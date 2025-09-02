package toy.recipit.common.util;

import org.springframework.stereotype.Component;
import toy.recipit.common.Constants;
import java.security.SecureRandom;

@Component
public class EmailVerificationCodeUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public String createVerificationCode(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(Constants.Email_VERIFICATION.CHAR_POOL
                    .charAt(RANDOM.nextInt(Constants.Email_VERIFICATION.CHAR_POOL.length())));
        }
        return sb.toString();
    }
}
