package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import toy.recipit.common.Constants;
import toy.recipit.common.util.EmailVerificationCodeUtil;
import toy.recipit.controller.dto.response.SendEmailAuthenticationDto;
import toy.recipit.mapper.EmailVerificationMapper;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationMapper emailVerificationMapper;
    private final JavaMailSender mailSender;
    private final EmailVerificationCodeUtil verificationCodeUtil;
    private static final int DEFAULT_VERIFICATION_CODE_LENGTH = 8;

    @Transactional(rollbackFor = Exception.class)
    public SendEmailAuthenticationDto sendEmailVerificationCode(String email) {
        boolean isSendEmailVerificationCode = emailVerificationMapper.isEmailExists(email);

        return isSendEmailVerificationCode ? resendEmailVerificationCode(email) : sendNewEmailVerificationCode(email);
    }

    private SendEmailAuthenticationDto sendNewEmailVerificationCode(String email) {
        String authenticationCode = verificationCodeUtil.createVerificationCode(DEFAULT_VERIFICATION_CODE_LENGTH);

        emailVerificationMapper.insertEmailVerification(
                email,
                authenticationCode,
                Constants.Email_VERIFICATION.ACTIVATE,
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

        String authenticationCode = verificationCodeUtil.createVerificationCode(DEFAULT_VERIFICATION_CODE_LENGTH);

        emailVerificationMapper.updateEmailVerification(
                email,
                authenticationCode,
                Constants.Email_VERIFICATION.ACTIVATE,
                Constants.System.SYSTEM_NUMBER
        );

        sendAuthenticationEmail(email, authenticationCode);

        LocalDateTime emailSendDatetime = emailVerificationMapper.getEditDateTime(email);

        return new SendEmailAuthenticationDto(true, emailSendDatetime);
    }

    private void sendAuthenticationEmail(String email, String code) {
        Runnable sendEmailTask = () -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[RECIPIT] 이메일 인증코드");
            message.setText("인증코드: " + code + "\n5분 내에 입력해주세요.");
            mailSender.send(message);
        };

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    sendEmailTask.run();
                }
            });
        } else {
            sendEmailTask.run();
        }
    }
}
