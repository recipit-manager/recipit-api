package toy.recipit.common.util;

import org.springframework.stereotype.Component;
import toy.recipit.common.Constants;
import java.security.SecureRandom;

@Component
public class EmailVerificationCodeGenerator {
    private final int DEFAULT_VERIFICATION_CODE_LENGTH = 8;
    private final SecureRandom RANDOM = new SecureRandom();

    public String createVerificationCode() {
        return createVerificationCode(DEFAULT_VERIFICATION_CODE_LENGTH);
    }

    private String createVerificationCode(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(Constants.RandomCode.UPPERCASE_ALPHABET_NUMBER
                    .charAt(RANDOM.nextInt(Constants.RandomCode.UPPERCASE_ALPHABET_NUMBER.length())));
        }

        return sb.toString();
    }
}
