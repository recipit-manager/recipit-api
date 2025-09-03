package toy.recipit.service;

import lombok.RequiredArgsConstructor;
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
        boolean isSendEmailVerificationCode = emailVerificationMapper.isEmailExists(email);

        return isSendEmailVerificationCode ? resendEmailVerificationCode(email) : sendNewEmailVerificationCode(email);
    }

    @Transactional
    public boolean checkEmailVerificationCode(String email, String code) {
        return emailVerificationMapper.getUserEmailVerification(email)
                .filter(userEmailVerification -> userEmailVerification.getVerifyingCode().equals(code))
                .filter(userEmailVerification -> userEmailVerification.getVerifyingExpireDatetime().isAfter(LocalDateTime.now()))
                .filter(userEmailVerification -> !Constants.EmailVerification.SUCCESS.equals(userEmailVerification.getVerifyingStatusCode()))
                .map(userEmailVerification -> {
                    emailVerificationMapper.updateEmailVerificationStatus(
                            email,
                            Constants.EmailVerification.SUCCESS,
                            Constants.System.SYSTEM_NUMBER
                    );
                    return true;
                })
                .orElse(false);
    }

    private SendEmailAuthenticationDto sendNewEmailVerificationCode(String email) {
        String authenticationCode = verificationCodeUtil.createVerificationCode();

        emailVerificationMapper.insertEmailVerification(
                email,
                authenticationCode,
                Constants.EmailVerification.ACTIVATE,
                Constants.System.SYSTEM_NUMBER
        );

        sendAuthenticationEmail(email, authenticationCode);

        return new SendEmailAuthenticationDto(true, emailVerificationMapper.getEditDateTime(email));
    }

    private SendEmailAuthenticationDto resendEmailVerificationCode(String email) {
        LocalDateTime lastEmailSendDateTime = emailVerificationMapper.getEditDateTime(email);
        LocalDateTime now = LocalDateTime.now();
        long secondsSinceEdit = Duration.between(lastEmailSendDateTime, now).getSeconds();

        if (secondsSinceEdit < 60) {
            return new SendEmailAuthenticationDto(false, lastEmailSendDateTime);
        }

        String authenticationCode = verificationCodeUtil.createVerificationCode();

        emailVerificationMapper.updateEmailVerification(
                email,
                authenticationCode,
                Constants.EmailVerification.ACTIVATE,
                Constants.System.SYSTEM_NUMBER
        );

        sendAuthenticationEmail(email, authenticationCode);

        LocalDateTime emailSendDatetime = emailVerificationMapper.getEditDateTime(email);

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
