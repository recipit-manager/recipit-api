package toy.recipit.service;

import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.common.Constants;
import toy.recipit.common.exception.UserNotFoundException;
import toy.recipit.common.exception.loginFailException;
import toy.recipit.common.util.EmailMaskUtil;
import toy.recipit.common.util.SecurityUtil;
import toy.recipit.common.util.TemporaryPasswordGenerator;
import toy.recipit.controller.dto.request.ChangePasswordDto;
import toy.recipit.controller.dto.request.ChangeTemporaryPasswordDto;
import toy.recipit.controller.dto.request.CommonCodeDto;
import toy.recipit.controller.dto.request.FindUserIdDto;
import toy.recipit.controller.dto.request.FindUserPasswordDto;
import toy.recipit.controller.dto.request.LoginDto;
import toy.recipit.controller.dto.request.SignUpDto;
import toy.recipit.controller.dto.response.AutoLoginResultDto;
import toy.recipit.controller.dto.response.CountryCodeDto;
import toy.recipit.controller.dto.response.LoginResultDto;
import toy.recipit.mapper.UserMapper;
import toy.recipit.mapper.vo.InsertUserVo;
import toy.recipit.mapper.vo.UserVo;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final CommonService commonService;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final EmailVerificationService emailVerificationService;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    public boolean isNicknameDuplicate(String nickname) {
        return userMapper.isNicknameDuplicate(nickname);
    }

    @Transactional
    public boolean signUp(SignUpDto signUpDto) {
        String email = signUpDto.getEmail();
        String emailHashing = DigestUtils.sha256Hex(email);
        String emailEncrypt = securityUtil.encrypt(email);

        String phoneNumber = signUpDto.getPhoneNumber();
        String phoneNumberHashing = DigestUtils.sha256Hex(phoneNumber);
        String phoneNumberEncrypt = securityUtil.encrypt(phoneNumber);

        validateNickname(signUpDto.getNickname());
        validateEmailVerification(emailHashing);
        validateDuplicateEmail(emailHashing);
        validateDuplicateNameAndPhone(signUpDto, phoneNumberHashing);
        validateCountryAndPhoneNumber(signUpDto.getCountryCode(), phoneNumber);

        InsertUserVo insertUserVo = new InsertUserVo(
                signUpDto,
                emailHashing,
                emailEncrypt,
                passwordEncoder.encode(signUpDto.getPassword()),
                phoneNumberHashing,
                phoneNumberEncrypt
        );

        userMapper.insertUser(insertUserVo);

        return true;
    }

    @Transactional(noRollbackFor = loginFailException.class)
    public LoginResultDto login(LoginDto loginDto) {
        String emailHashing = DigestUtils.sha256Hex(loginDto.getEmail());

        UserVo userVo = userMapper.getUserByEmail(emailHashing)
                .orElseThrow(() -> new UserNotFoundException("login.notFoundUser"));

        checkUserStatus(userVo.getStatusCode());
        checkPassword(loginDto, userVo);

        if (userVo.getLoginFailCount() != 0) {
            resetLoginFailCount(emailHashing);
        }

        String autoLoginToken;

        autoLoginToken = loginDto.isAutoLogin() ?
                createAutoLoginToken(userVo.getUserNo()) : StringUtils.EMPTY;

        return new LoginResultDto(
                userVo.getUserNo(),
                userVo.getNickName(),
                userVo.getStatusCode(),
                autoLoginToken);
    }

    public boolean isExistAutoLoginTokenAndRemove(String autoLoginToken) {
        if(redisTemplate.hasKey(autoLoginToken)) {
            redisTemplate.unlink(autoLoginToken);
            return true;
        } else {
            return false;
        }
    }

    public AutoLoginResultDto autoLogin(String autoLoginToken) {
        String userNo = redisTemplate.opsForValue().get(autoLoginToken);

        if (userNo == null) {
           return new AutoLoginResultDto(true);
        }

        UserVo userVo = userMapper.getUserByUserNo(userNo)
                .orElseThrow(() -> new UserNotFoundException("login.notFoundUser"));

        return new AutoLoginResultDto(
                false,
                userVo.getNickName(),
                userNo,
                userVo.getStatusCode(),
                refreshAutoLoginToken(autoLoginToken, userNo));
    }

    public String findUserId(FindUserIdDto findUserIdDto) {
        validateCountryAndPhoneNumber(findUserIdDto.getCountryCode(), findUserIdDto.getPhoneNumber());

        UserVo userVo = userMapper.getUserForFindAccount(
                StringUtil.EMPTY_STRING,
                findUserIdDto.getFirstName(),
                findUserIdDto.getMiddleName(),
                findUserIdDto.getLastName(),
                DigestUtils.sha256Hex(findUserIdDto.getPhoneNumber())
        ).orElseThrow(() -> new UserNotFoundException("findUserAccount.notFoundUser"));

        String userEmail = securityUtil.decrypt(userVo.getEmailEncrypt());

        return EmailMaskUtil.emailMasking(userEmail);
    }

    @Transactional
    public Boolean sendTemporaryPassword(FindUserPasswordDto findUserPasswordDto) {
        validateCountryAndPhoneNumber(findUserPasswordDto.getCountryCode(), findUserPasswordDto.getPhoneNumber());

        UserVo userVo = userMapper.getUserForFindAccount(
                DigestUtils.sha256Hex(findUserPasswordDto.getEmail()),
                findUserPasswordDto.getFirstName(),
                findUserPasswordDto.getMiddleName(),
                findUserPasswordDto.getLastName(),
                DigestUtils.sha256Hex(findUserPasswordDto.getPhoneNumber())
        ).orElseThrow(() -> new UserNotFoundException("findUserAccount.notFoundUser"));

        String temporaryPassword = TemporaryPasswordGenerator.passwordGenerate();

        userMapper.updatePassword(
                userVo.getUserNo(),
                passwordEncoder.encode(temporaryPassword),
                Constants.UserStatus.STOP,
                Constants.SystemId.SYSTEM_NUMBER
        );

        sendTemporaryPasswordEmail(findUserPasswordDto.getEmail(), temporaryPassword);

        return true;
    }

    @Transactional
    public Boolean changeTemporaryPassword(String userNo, ChangeTemporaryPasswordDto changeTemporaryPasswordDto) {
        userMapper.updatePassword(
                userNo,
                passwordEncoder.encode(changeTemporaryPasswordDto.getPassword()),
                Constants.UserStatus.ACTIVE,
                userNo
        );

        return true;
    }

    @Transactional
    public Boolean changePassword(String userNo, ChangePasswordDto changePasswordDto) {
        UserVo userVo = userMapper.getUserByUserNo(userNo)
                .orElseThrow(() -> new UserNotFoundException("findUserAccount.notFoundUser"));

        if (passwordEncoder.matches(changePasswordDto.getPassword(), userVo.getPassword())) {
            throw new IllegalArgumentException("changePassword.sameAsCurrentPassword");
        }

        if (passwordEncoder.matches(changePasswordDto.getCurrentPassword(), userVo.getPassword())) {
            userMapper.updatePassword(
                    userNo,
                    passwordEncoder.encode(changePasswordDto.getPassword()),
                    Constants.UserStatus.ACTIVE,
                    userNo
            );
        } else {
            throw new IllegalArgumentException("changePassword.wrongPassword");
        }

        return true;
    }

    private String refreshAutoLoginToken(String autoLoginToken, String userNo) {
        redisTemplate.unlink(autoLoginToken);

        String newAutoLoginToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
                newAutoLoginToken,
                userNo,
                Constants.UserLogin.AUTO_LOGIN_EXPIRATION_DAYS,
                TimeUnit.DAYS
        );

        return newAutoLoginToken;
    }


    private void validateNickname(String nickname) {
        if (isNicknameDuplicate(nickname)) {
            throw new IllegalArgumentException("signUp.duplicateNickname");
        }
    }

    private void validateEmailVerification(String emailHashing) {
        if (emailVerificationService.isEmailVerificationFail(emailHashing)) {
            throw new IllegalArgumentException("signUp.emailVerificationFailed");
        }
    }

    private void validateDuplicateEmail(String emailHashing) {
        if (userMapper.isEmailExists(emailHashing)) {
            throw new IllegalArgumentException("signUp.duplicateEmail");
        }
    }

    private void validateDuplicateNameAndPhone(SignUpDto signUpDto, String phoneNumberHashing) {
        if (userMapper.isNameAndPhoneNumberExists(
                signUpDto.getFirstName(),
                signUpDto.getMiddleName(),
                signUpDto.getLastName(),
                phoneNumberHashing)) {
            throw new IllegalArgumentException("signUp.duplicateNameAndPhoneNumber");
        }
    }

    private void validateCountryAndPhoneNumber(
            CommonCodeDto commonCodeDto,
            String phoneNumber
    ) {
        Optional<CountryCodeDto> countryCodeDto = commonService.getCountryCode(
                commonCodeDto.getGroupCode(), commonCodeDto.getCode());

        if (countryCodeDto.isEmpty()) {
            throw new IllegalArgumentException("signUp.invalidCountryCode");
        }

        if (!phoneNumber.matches(countryCodeDto.get().getRegex())) {
            throw new IllegalArgumentException("signUp.invalidPhoneNumber");
        }
    }

    private void checkUserStatus(String statusCode) {
        if (Constants.UserStatus.INACTIVE.equals(statusCode)) {
            throw new loginFailException("login.inactiveUser");
        }
    }

    private void checkPassword(LoginDto loginDto, UserVo userVo) {
        if (!passwordEncoder.matches(loginDto.getPassword(), userVo.getPassword())) {
            userMapper.increaseLoginFailCount(userVo.getEmailHashing(), Constants.SystemId.SYSTEM_NUMBER);

            if (userVo.getLoginFailCount() + 1 >= Constants.UserLogin.LOGIN_FAIL_INACTIVE_THRESHOLD) {
                userMapper.updateStatusCode(
                        userVo.getEmailHashing(),
                        Constants.UserStatus.INACTIVE,
                        Constants.SystemId.SYSTEM_NUMBER
                );
            }
            throw new loginFailException("login.notFoundUser");
        }
    }

    private void resetLoginFailCount(String emailHashing) {
        userMapper.resetLoginFailCount(emailHashing, Constants.SystemId.SYSTEM_NUMBER);
    }

    private String createAutoLoginToken(String userNo) {
        String autoLoginToken = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                autoLoginToken,
                userNo,
                Constants.UserLogin.AUTO_LOGIN_EXPIRATION_DAYS,
                TimeUnit.DAYS
        );

        return autoLoginToken;
    }

    private void sendTemporaryPasswordEmail(String email, String temporalPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[RECIPIT] 비밀번호 재설정");
        message.setText("비밀번호: " + temporalPassword + "\n해당 임시 비밀번호로 로그인한 후 새로운 비밀번호로 변경이 필요합니다");
        mailSender.send(message);
    }
}