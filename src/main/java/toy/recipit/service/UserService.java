package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.common.Constants;
import toy.recipit.common.util.SecurityUtil;
import toy.recipit.controller.dto.request.SignUpDto;
import toy.recipit.mapper.CommonMapper;
import toy.recipit.mapper.EmailVerificationMapper;
import toy.recipit.mapper.UserMapper;
import toy.recipit.mapper.vo.CommonDetailCodeVo;
import toy.recipit.mapper.vo.UserEmailVerification;
import toy.recipit.mapper.vo.UserVo;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final EmailVerificationMapper emailVerificationMapper;
    private final CommonMapper commonMapper;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public boolean isNicknameDuplicate(String nickname) {
        return userMapper.isNicknameDuplicate(nickname);
    }

    @Transactional
    public boolean signUp(SignUpDto signUpDto) {
        if (isNicknameDuplicate(signUpDto.getNickname())) {
            return false;
        }

        String hashingEmail = DigestUtils.sha256Hex(signUpDto.getEmail());
        Optional<UserEmailVerification> verificationOpt = emailVerificationMapper.getUserEmailVerification(hashingEmail);

        if (verificationOpt.isEmpty()
                || !Constants.EmailVerification.SUCCESS.equals(verificationOpt.get().getVerifyingStatusCode())) {
            return false;
        }

        String emailPacked = securityUtil.encryptWithToken(signUpDto.getEmail());
        String phonePacked = securityUtil.encryptWithToken(signUpDto.getPhoneNumber());

        String emailToken = securityUtil.extractToken(emailPacked).orElseThrow();
        String phoneToken = securityUtil.extractToken(phonePacked).orElseThrow();

        if (userMapper.isExistsByEmailForSignUp(emailToken)) {
            return false;
        }

        if (userMapper.isExistsByNameAndPhoneForSignUp(
                signUpDto.getFirstName(),
                signUpDto.getMiddleName(),
                signUpDto.getLastName(),
                phoneToken
        )) {
            return false;
        }

        Optional<CommonDetailCodeVo> commonDetailCodeVoOpt = commonMapper.getCommonDetailCodeByCode(signUpDto.getCountryCode());
        if (commonDetailCodeVoOpt.isEmpty()) {
            return false;
        }

        if (!signUpDto.getPhoneNumber().matches(commonDetailCodeVoOpt.get().getNote3())) {
            return false;
        }

        UserVo user = UserVo.builder()
                .email(emailPacked)
                .nickName(signUpDto.getNickname())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .lastName(signUpDto.getLastName())
                .middleName(signUpDto.getMiddleName())
                .firstName(signUpDto.getFirstName())
                .phoneNumber(phonePacked)
                .countryCode(signUpDto.getCountryCode())
                .loginFailCount(Constants.User.LOGIN_FAIL_COUNT_INITIAL)
                .statusCode(Constants.User.STATUS_ACTIVE)
                .build();

        userMapper.insertUser(user, Constants.SystemId.SYSTEM_NUMBER);

        return true;
    }
}
