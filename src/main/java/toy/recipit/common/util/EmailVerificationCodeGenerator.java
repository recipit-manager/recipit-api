package toy.recipit.common.util;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Component;
import toy.recipit.common.Constants;
import java.security.SecureRandom;

@Component
public class EmailVerificationCodeGenerator {
    private final int MAX_VERIFICATION_CODE_LENGTH = 32;
    private final int DEFAULT_VERIFICATION_CODE_LENGTH = 8;
    private final SecureRandom RANDOM = new SecureRandom();

    public String createVerificationCode() {
        return createVerificationCode(DEFAULT_VERIFICATION_CODE_LENGTH);
    }

    public String createVerificationCode(
            @Min(value = 1,  message = "validation.verification_code.blank")
            @Max(value = MAX_VERIFICATION_CODE_LENGTH, message = "validation.verification_code.size")
            int length
    ) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(Constants.RandomCode.CHAR_POOL_WITHOUT_SIMILAR
                    .charAt(RANDOM.nextInt(Constants.RandomCode.CHAR_POOL_WITHOUT_SIMILAR.length())));
        }
        return sb.toString();
    }
}
