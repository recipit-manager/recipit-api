package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.common.Constants;
import toy.recipit.common.util.SecurityUtil;
import toy.recipit.controller.dto.request.CommonCodeDto;
import toy.recipit.controller.dto.request.LoginDto;
import toy.recipit.controller.dto.request.SignUpDto;
import toy.recipit.controller.dto.response.CountryCodeDto;
import toy.recipit.controller.dto.response.LoginResult;
import toy.recipit.controller.dto.response.SessionUser;
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

    public LoginResult login(LoginDto loginDto) {
        String emailHashing = DigestUtils.sha256Hex(loginDto.getEmail());

        UserVo userVo = userMapper.getUserByEmail(emailHashing)
                .orElseThrow(() -> new IllegalArgumentException("login.notFoundUser"));

        checkUserStatus(userVo.getStatusCode());
        checkPassword(loginDto, userVo);

        if (userVo.getLoginFailCount() != 0) {
            resetLoginFailCount(emailHashing);
        }

        String autoLoginToken;

        if (loginDto.isAutoLogin()) {
            autoLoginToken = createAutoLoginToken(userVo.getUserNo());
        } else {
            autoLoginToken = StringUtils.EMPTY;
        }

        SessionUser sessionUser = new SessionUser(
                userVo.getUserNo(),
                userVo.getNickName(),
                userVo.getStatusCode()
        );

        return new LoginResult(sessionUser, autoLoginToken);
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
            throw new IllegalArgumentException("login.inactiveUser");
        }
    }

    @Transactional
    public void checkPassword(LoginDto loginDto, UserVo userVo) {
        if (!passwordEncoder.matches(loginDto.getPassword(), userVo.getPassword())) {
            userMapper.increaseLoginFailCount(userVo.getEmailHashing(), Constants.SystemId.SYSTEM_NUMBER);

            if (userVo.getLoginFailCount() >= 4) {
                userMapper.updateStatusCode(userVo.getEmailHashing(), Constants.UserStatus.INACTIVE, Constants.SystemId.SYSTEM_NUMBER);
            }

            throw new IllegalArgumentException("login.notFoundUser");
        }
    }

    @Transactional
    public void resetLoginFailCount(String emailHashing) {
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
}