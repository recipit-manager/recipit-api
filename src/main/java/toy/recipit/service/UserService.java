package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import toy.recipit.controller.responseDto.SendEmailAuthenticationDto;
import toy.recipit.mapper.UserMapper;

import java.security.SecureRandom;

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

        //if (dbEmailExists) {
            // TODO: 이미 존재하는 경우 쿨타임 체크, 재발송 update
            //return resendEmail(email);
        //} else {
            // TODO: 신규 이메일 insert
            return sendEmail(email);
        //}
    }

    private SendEmailAuthenticationDto sendEmail(String email) {
        String authenticationCode = createVerificationCode(8);

        sendEmail(email, authenticationCode);

        userMapper.insertEmailVerification(email, authenticationCode);

        return new SendEmailAuthenticationDto(true, userMapper.getExpireDateTimeByEmail(email));
    }

//    private EmailAuthenticationResultDto resendEmail(String email) {
//
//    }

    private String createVerificationCode(int length) {
        final String CHAR_POOL = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return stringBuilder.toString();
    }

    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Recipit] 이메일 인증코드");
        message.setText("인증코드: " + code + "\n5분 내에 입력해주세요.");
        mailSender.send(message);
    }
}
