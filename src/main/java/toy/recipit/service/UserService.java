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
    private final CommonService commonService;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final EmailVerificationService emailVerificationService;

    public boolean isNicknameDuplicate(String nickname) {
        return userMapper.isNicknameDuplicate(nickname);
    }

    @Transactional
    public boolean signUp(SignUpDto signUpDto) {
        if (isNicknameDuplicate(signUpDto.getNickname())) {
            return false;
        }

        String hashingEmail = DigestUtils.sha256Hex(signUpDto.getEmail());

        if (!emailVerificationService.isEmailVerificationSuccess(hashingEmail)) {
            return false;
        }

        String emailPacked = securityUtil.encryptWithHasing(signUpDto.getEmail())
                .orElseThrow(IllegalArgumentException::new);
        String phonePacked = securityUtil.encryptWithHasing(signUpDto.getPhoneNumber())
                .orElseThrow(IllegalArgumentException::new);

        String emailToken = securityUtil.extractHasing(emailPacked).orElseThrow();
        String phoneToken = securityUtil.extractHasing(phonePacked).orElseThrow();

        if (userMapper.isEmailExists(emailToken)) {
            return false;
        }

        if (userMapper.isNameAndPhoneExists(
                signUpDto.getFirstName(),
                signUpDto.getMiddleName(),
                signUpDto.getLastName(),
                phoneToken
        )) {
            return false;
        }

        Optional<CommonDetailCodeVo> commonDetailCodeVo = commonService.getCommonDetailCode(signUpDto.getGroupCode(), signUpDto.getCountryCode());
        if (commonDetailCodeVo.isEmpty()) {
            return false;
        }

        if (!signUpDto.getPhoneNumber().matches(commonDetailCodeVo.get().getNote3())) {
            return false;
        }

        UserVo user = new UserVo(
                null,
                emailPacked,
                passwordEncoder.encode(signUpDto.getPassword()),
                signUpDto.getFirstName(),
                signUpDto.getMiddleName(),
                signUpDto.getLastName(),
                signUpDto.getNickname(),
                signUpDto.getCountryCode(),
                phonePacked,
                Constants.User.LOGIN_FAIL_COUNT_INITIAL,
                Constants.User.STATUS_ACTIVE
        );

        userMapper.insertUser(user, Constants.SystemId.SYSTEM_NUMBER);

        return true;
    }
}
