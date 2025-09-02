package toy.recipit.common.util;

import org.springframework.stereotype.Component;
import toy.recipit.common.Constants;
import java.security.SecureRandom;

@Component
public class EmailVerificationCodeUtil {
    private static final int DEFAULT_VERIFICATION_CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String createVerificationCode() {
        return createVerificationCode(DEFAULT_VERIFICATION_CODE_LENGTH);
    }

    public String createVerificationCode(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(Constants.RandomCode.CHAR_POOL_WITHOUT_SIMILAR
                    .charAt(RANDOM.nextInt(Constants.RandomCode.CHAR_POOL_WITHOUT_SIMILAR.length())));
        }
        return sb.toString();
    }
}
