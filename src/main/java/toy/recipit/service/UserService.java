package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.response.SendEmailAuthenticationDto;
import toy.recipit.mapper.EmailVerificationMapper;
import toy.recipit.mapper.UserMapper;
import java.time.LocalDateTime;

import static toy.recipit.common.util.VerificationCodeUtil.createVerificationCode;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final EmailVerificationMapper emailVerificationMapper;
    private final JavaMailSender mailSender;

    public boolean isNicknameDuplicate(String nickname) {
        return userMapper.isNicknameDuplicate(nickname);
    }

    public SendEmailAuthenticationDto sendEmailVerificationCode(String email) {
        boolean dbEmailExists = emailVerificationMapper.isEmailExists(email);

        return dbEmailExists ? resendEmail(email) : sendEmail(email);
    }

    private SendEmailAuthenticationDto sendEmail(String email) {
        String authenticationCode = createVerificationCode(Constants.Email.VERIFICATION_LENGTH);

        sendAuthenticationEmail(email, authenticationCode);

        emailVerificationMapper.insertEmailVerification(email, authenticationCode);

        return new SendEmailAuthenticationDto(true, emailVerificationMapper.getEditDateTime(email));
    }

    private SendEmailAuthenticationDto resendEmail(String email) {
        LocalDateTime lastEmailSendDateTime = emailVerificationMapper.getEditDateTime(email);
        LocalDateTime now = LocalDateTime.now();
        long secondsSinceEdit = java.time.Duration.between(lastEmailSendDateTime, now).getSeconds();

        if (secondsSinceEdit < 60) {
            return new SendEmailAuthenticationDto(false, lastEmailSendDateTime);
        }

        String authenticationCode = createVerificationCode(Constants.Email.VERIFICATION_LENGTH);
        sendAuthenticationEmail(email, authenticationCode);
        emailVerificationMapper.updateEmailVerification(email, authenticationCode);
        LocalDateTime postDateTime = emailVerificationMapper.getEditDateTime(email);

        return new SendEmailAuthenticationDto(true, postDateTime);
    }

    private void sendAuthenticationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[RECIPIT] 이메일 인증코드");
        message.setText("인증코드: " + code + "\n5분 내에 입력해주세요.");
        mailSender.send(message);
    }
}
