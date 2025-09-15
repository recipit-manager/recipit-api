package toy.recipit.common.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailMaskUtil {
    private final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public String emailMasking(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException();
        }

        String[] parts = email.split("@", 2);
        String id = parts[0];
        String domain = parts[1];

        if (id.length() <= 2) {
            return "*".repeat(id.length()) + "@" + domain;
        } else {
            String visible = id.substring(0, 2);
            String masked = "*".repeat(id.length() - 2);
            return visible + masked + "@" + domain;
        }
    }
}