package toy.recipit.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    private final HttpSession httpSession;
    private final StringRedisTemplate redisTemplate;
    private final HttpServletResponse response;

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

    @Transactional
    public boolean login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String emailHashing = DigestUtils.sha256Hex(email);

        UserVo userVo = getUserVoByEmail(emailHashing);

        checkUserStatus(userVo.getStatusCode());
        checkLoginFailCount(loginDto, userVo, emailHashing);

        if (userVo.getLoginFailCount() != 0) {
            resetLoginFailCount(emailHashing);
        }

        if (loginDto.isAutoLogin()) {
            setAutoLoginToken(userVo.getUserNo());
        }

        setSessionUser(userVo);

        return true;
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

    private UserVo getUserVoByEmail(String emailHashing) {
        return userMapper.getUserByEmail(emailHashing)
                .orElseThrow(() -> new IllegalArgumentException("login.notFoundUser"));
    }

    private void checkUserStatus(String statusCode) {
        if (Constants.UserStatus.INACTIVE.equals(statusCode)) {
            throw new IllegalArgumentException("login.inactiveUser");
        }
    }

    private void checkLoginFailCount(LoginDto loginDto, UserVo userVo, String emailHashing) {
        if (!passwordEncoder.matches(loginDto.getPassword(), userVo.getPassword())) {
            userMapper.increaseLoginFailCount(emailHashing, Constants.SystemId.SYSTEM_NUMBER);

            if (userVo.getLoginFailCount() >= 4) {
                userMapper.updateStatusCode(emailHashing, Constants.UserStatus.INACTIVE, Constants.SystemId.SYSTEM_NUMBER);
            }

            throw new IllegalArgumentException("login.notFoundUser");
        }
    }

    private void resetLoginFailCount(String emailHashing) {
        userMapper.resetLoginFailCount(emailHashing, Constants.SystemId.SYSTEM_NUMBER);
    }

    private void setSessionUser(UserVo userVo) {
        SessionUser sessionUser = new SessionUser(
                userVo.getUserNo(),
                userVo.getNickName(),
                userVo.getStatusCode()
        );
        httpSession.setAttribute("user", sessionUser);
    }

    private void setAutoLoginToken(String userNo) {
        String autoLoginToken = UUID.randomUUID().toString();

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(autoLoginToken, userNo, Constants.UserLogin.AUTO_LOGIN_EXPIRATION_DAYS, TimeUnit.DAYS);

        Cookie cookie = new Cookie(Constants.UserLogin.AUTO_LOGIN_COOKIE_NAME, autoLoginToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(Constants.UserLogin.AUTO_LOGIN_EXPIRATION_DAYS));
        response.addCookie(cookie);
    }
}