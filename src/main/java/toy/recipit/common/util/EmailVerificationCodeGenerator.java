package toy.recipit.common.util;

import org.springframework.stereotype.Component;
import toy.recipit.common.Constants;
import java.security.SecureRandom;

@Component
public class EmailVerificationCodeGenerator {
    private static final int MAX_VERIFICATION_CODE_LENGTH = 32;
    private static final int DEFAULT_VERIFICATION_CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String createVerificationCode() {
        return createVerificationCode(DEFAULT_VERIFICATION_CODE_LENGTH);
    }

    public String createVerificationCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("validation.verification_code.blank");
        }

        if (length > MAX_VERIFICATION_CODE_LENGTH) {
            throw new IllegalArgumentException("validation.verification_code.size");
        }

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(Constants.RandomCode.CHAR_POOL_WITHOUT_SIMILAR
                    .charAt(RANDOM.nextInt(Constants.RandomCode.CHAR_POOL_WITHOUT_SIMILAR.length())));
        }
        return sb.toString();
    }
}
