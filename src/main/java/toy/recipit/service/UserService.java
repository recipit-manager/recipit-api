package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import toy.recipit.controller.responseDto.SendEmailAuthenticationDto;
import toy.recipit.mapper.UserMapper;

import java.security.SecureRandom;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final JavaMailSender mailSender;

    public boolean isNicknameDuplicate(String nickname) {
        return userMapper.isNicknameDuplicate(nickname);
    }

    public SendEmailAuthenticationDto sendEmailVerificationCode(String email) {
        boolean dbEmailExists = userMapper.checkExistsByEmail(email);

        return dbEmailExists ? resendEmail(email) : sendEmail(email);
    }

    private SendEmailAuthenticationDto sendEmail(String email) {
        String authenticationCode = createVerificationCode(8);

        sendAuthenticationEmail(email, authenticationCode);

        userMapper.insertEmailVerification(email, authenticationCode);

        return new SendEmailAuthenticationDto(true, userMapper.getPostDateTimeByEmail(email));
    }

    private SendEmailAuthenticationDto resendEmail(String email) {
        OffsetDateTime lastEdited = userMapper.getPostDateTimeByEmail(email);
        OffsetDateTime now = OffsetDateTime.now();
        long secondsSinceEdit = java.time.Duration.between(lastEdited, now).getSeconds();

        if (secondsSinceEdit < 60) {
            return new SendEmailAuthenticationDto(false, lastEdited);
        }

        String authenticationCode = createVerificationCode(8);
        sendAuthenticationEmail(email, authenticationCode);
        userMapper.updateEmailVerification(email, authenticationCode);
        OffsetDateTime postDateTime = userMapper.getPostDateTimeByEmail(email);

        return new SendEmailAuthenticationDto(true, postDateTime);
    }

    private String createVerificationCode(int length) {
        final String CHAR_POOL = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            stringBuilder.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return stringBuilder.toString();
    }

    private void sendAuthenticationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[RECIPIT] 이메일 인증코드");
        message.setText("인증코드: " + code + "\n5분 내에 입력해주세요.");
        mailSender.send(message);
    }
}
