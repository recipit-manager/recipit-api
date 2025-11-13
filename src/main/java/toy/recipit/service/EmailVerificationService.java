package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.recipit.common.Constants;
import toy.recipit.common.util.EmailVerificationCodeGenerator;
import toy.recipit.controller.dto.response.SendEmailAuthenticationDto;
import toy.recipit.mapper.EmailVerificationMapper;
import toy.recipit.mapper.vo.UserEmailVerification;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationMapper emailVerificationMapper;
    private final JavaMailSender mailSender;
    private final EmailVerificationCodeGenerator verificationCodeUtil;

    @Transactional(rollbackFor = Exception.class)
    public SendEmailAuthenticationDto sendEmailVerificationCode(String email) {
        String emailHasing = DigestUtils.sha256Hex(email);
        boolean isSendEmailVerificationCode = emailVerificationMapper.isEmailExists(emailHasing);

        return isSendEmailVerificationCode ? resendEmailVerificationCode(email, emailHasing) : sendNewEmailVerificationCode(email, emailHasing);
    }

    @Transactional
    public boolean checkEmailVerificationCode(String email, String code) {
        String emailHashing = DigestUtils.sha256Hex(email);
        Optional<UserEmailVerification> userEmailVerificationOpt = emailVerificationMapper.getUserEmailVerification(emailHashing);

        if (userEmailVerificationOpt.isEmpty()) {
            return false;
        }

        UserEmailVerification userEmailVerification = userEmailVerificationOpt.get();

        if (!userEmailVerification.getVerifyingCode().equals(code)) {
            return false;
        }

        if (userEmailVerification.getVerifyingExpireDatetime().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (Constants.EmailVerification.SUCCESS.equals(userEmailVerification.getVerifyingStatusCode())) {
            return false;
        }


        emailVerificationMapper.updateEmailVerificationStatus(
                emailHashing,
                Constants.EmailVerification.SUCCESS,
                Constants.SystemId.SYSTEM_NUMBER
        );

        return true;
    }

    public boolean isEmailVerificationFail(String emailHashing) {
        Optional<UserEmailVerification> userEmailVerification = emailVerificationMapper.getUserEmailVerification(emailHashing);

        if (userEmailVerification.isEmpty()) {
            return true;
        }

        if (!Constants.EmailVerification.SUCCESS.equals(userEmailVerification.get().getVerifyingStatusCode())) {
            return true;
        }

        return false;
    }

    private SendEmailAuthenticationDto sendNewEmailVerificationCode(String email, String emailHasing) {
        String authenticationCode = verificationCodeUtil.createVerificationCode();

        emailVerificationMapper.insertEmailVerification(
                emailHasing,
                authenticationCode,
                Constants.EmailVerification.ACTIVATE,
                Constants.SystemId.SYSTEM_NUMBER
        );

        sendAuthenticationEmail(email, authenticationCode);

        return new SendEmailAuthenticationDto(true, emailVerificationMapper.getEditDateTime(emailHasing));
    }

    private SendEmailAuthenticationDto resendEmailVerificationCode(String email, String emailHasing) {
        LocalDateTime lastEmailSendDateTime = emailVerificationMapper.getEditDateTime(emailHasing);
        LocalDateTime now = LocalDateTime.now();
        long secondsSinceEdit = Duration.between(lastEmailSendDateTime, now).getSeconds();

        if (secondsSinceEdit < 60) {
            return new SendEmailAuthenticationDto(false, lastEmailSendDateTime);
        }

        String authenticationCode = verificationCodeUtil.createVerificationCode();

        emailVerificationMapper.updateEmailVerification(
                emailHasing,
                authenticationCode,
                Constants.EmailVerification.ACTIVATE,
                Constants.SystemId.SYSTEM_NUMBER
        );

        sendAuthenticationEmail(email, authenticationCode);

        LocalDateTime emailSendDatetime = emailVerificationMapper.getEditDateTime(emailHasing);

        return new SendEmailAuthenticationDto(true, emailSendDatetime);
    }

    private void sendAuthenticationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[RECIPIT] 이메일 인증코드");
        message.setText("인증코드: " + code + "\n5분 내에 입력해주세요.");
        mailSender.send(message);
    }
}
