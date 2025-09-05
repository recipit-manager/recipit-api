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
import toy.recipit.controller.dto.response.CountryCodeDto;
import toy.recipit.mapper.EmailVerificationMapper;
import toy.recipit.mapper.UserMapper;
import toy.recipit.mapper.vo.CommonDetailCodeVo;
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

        if (emailVerificationService.isEmailVerificationFail(hashingEmail)) {
            return false;
        }

        String emailHash = DigestUtils.sha256Hex(signUpDto.getEmail());
        String emailEncrypt = securityUtil.encrypt(signUpDto.getEmail())
                .orElseThrow(IllegalArgumentException::new);

        String phoneNumberHash = DigestUtils.sha256Hex(signUpDto.getPhoneNumber());
        String phoneNumberEncrypt = securityUtil.encrypt(signUpDto.getPhoneNumber())
                .orElseThrow(IllegalArgumentException::new);

        if (userMapper.isEmailExists(emailHash)) {
            return false;
        }

        if (userMapper.isNameAndPhoneExists(
                signUpDto.getFirstName(),
                signUpDto.getMiddleName(),
                signUpDto.getLastName(),
                phoneNumberHash
        )) {
            return false;
        }


        Optional<CountryCodeDto> countryCodeDto = commonService.getCountryCode(
                signUpDto.getGroupCode(),
                signUpDto.getCountryCode().getCode()
        );


        if (countryCodeDto.isEmpty()) {
            return false;
        }

        if (!signUpDto.getPhoneNumber().matches(countryCodeDto.get().getRegex())) {
            return false;
        }

        UserVo user = new UserVo(
                null,
                emailHash,
                emailEncrypt,
                passwordEncoder.encode(signUpDto.getPassword()),
                signUpDto.getFirstName(),
                signUpDto.getMiddleName(),
                signUpDto.getLastName(),
                signUpDto.getNickname(),
                signUpDto.getCountryCode().getCode(),
                phoneNumberHash,
                phoneNumberEncrypt,
                Constants.User.LOGIN_FAIL_COUNT_INITIAL,
                Constants.User.STATUS_ACTIVE
        );


        userMapper.insertUser(user, Constants.SystemId.SYSTEM_NUMBER);

        return true;
    }
}
