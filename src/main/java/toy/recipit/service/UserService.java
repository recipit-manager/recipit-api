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
import toy.recipit.controller.dto.request.CommonCodeDto;
import toy.recipit.controller.dto.request.SignUpDto;
import toy.recipit.controller.dto.response.CountryCodeDto;
import toy.recipit.mapper.UserMapper;
import toy.recipit.mapper.vo.InsertUserVo;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final CommonService commonService;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final EmailVerificationService emailVerificationService;

    public boolean isNicknameDuplicate(String nickname) {
        return userMapper.isNicknameDuplicate(nickname);
    }

    @Transactional
    public boolean signUp(SignUpDto signUpDto) {
        validateNickname(signUpDto.getNickname());

        String email = signUpDto.getEmail();
        String emailHashing = DigestUtils.sha256Hex(email);
        validateEmailVerification(emailHashing);
        String emailEncrypt = securityUtil.encrypt(email);

        String phoneNumber = signUpDto.getPhoneNumber();
        String phoneNumberHashing = DigestUtils.sha256Hex(phoneNumber);
        String phoneNumberEncrypt = securityUtil.encrypt(phoneNumber);

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

        userMapper.insertUser(insertUserVo, Constants.SystemId.SYSTEM_NUMBER);

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
}