package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.common.Constants;
import toy.recipit.common.util.SecurityUtil;
import toy.recipit.controller.dto.request.SignUpDto;
import toy.recipit.controller.dto.response.CountryCodeDto;
import toy.recipit.controller.dto.response.SignUpResultDto;
import toy.recipit.mapper.UserMapper;
import toy.recipit.mapper.vo.insertUserVo;

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
    private final MessageSource messageSource;


    public boolean isNicknameDuplicate(String nickname) {
        return userMapper.isNicknameDuplicate(nickname);
    }

    @Transactional
    public SignUpResultDto signUp(SignUpDto signUpDto) {
        if (isNicknameDuplicate(signUpDto.getNickname())) {
            String message = messageSource.getMessage("signUp.duplicateNickname", null, LocaleContextHolder.getLocale());
            return new SignUpResultDto(false, message);
        }


        if (emailVerificationService.isEmailVerificationFail(signUpDto.getEmail())) {
            String message = messageSource.getMessage("signUp.emailVerificationFailed", null, LocaleContextHolder.getLocale());
            return new SignUpResultDto(false, message);
        }


        String emailHashing = DigestUtils.sha256Hex(signUpDto.getEmail());
        String emailEncrypt = securityUtil.encrypt(signUpDto.getEmail())
                .orElseThrow(IllegalArgumentException::new);

        String phoneNumberHashing = DigestUtils.sha256Hex(signUpDto.getPhoneNumber());
        String phoneNumberEncrypt = securityUtil.encrypt(signUpDto.getPhoneNumber())
                .orElseThrow(IllegalArgumentException::new);

        if (userMapper.isEmailExists(emailHashing)) {
            String message = messageSource.getMessage("signUp.duplicateEmail", null, LocaleContextHolder.getLocale());
            return new SignUpResultDto(false, message);
        }


        if (userMapper.isNameAndPhoneExists(
                signUpDto.getFirstName(),
                signUpDto.getMiddleName(),
                signUpDto.getLastName(),
                phoneNumberHashing
        )) {
            String message = messageSource.getMessage("signUp.duplicateNameAndPhone", null, LocaleContextHolder.getLocale());
            return new SignUpResultDto(false, message);
        }



        Optional<CountryCodeDto> countryCodeDto = commonService.getCountryCode(
                signUpDto.getGroupCode(),
                signUpDto.getCountryCode().getCode()
        );


        if (countryCodeDto.isEmpty()) {
            String message = messageSource.getMessage("signUp.invalidCountryCode", null, LocaleContextHolder.getLocale());
            return new SignUpResultDto(false, message);
        }


        if (!signUpDto.getPhoneNumber().matches(countryCodeDto.get().getRegex())) {
            String message = messageSource.getMessage("signUp.invalidPhoneNumber", null, LocaleContextHolder.getLocale());
            return new SignUpResultDto(false, message);
        }


        insertUserVo user = new insertUserVo(
                null,
                emailHashing,
                emailEncrypt,
                passwordEncoder.encode(signUpDto.getPassword()),
                signUpDto.getFirstName(),
                signUpDto.getMiddleName(),
                signUpDto.getLastName(),
                signUpDto.getNickname(),
                signUpDto.getCountryCode().getCode(),
                phoneNumberHashing,
                phoneNumberEncrypt
        );


        userMapper.insertUser(user, Constants.SystemId.SYSTEM_NUMBER);

        String successMessage = messageSource.getMessage("signUp.success", null, LocaleContextHolder.getLocale());
        return new SignUpResultDto(true, successMessage);

    }
}
