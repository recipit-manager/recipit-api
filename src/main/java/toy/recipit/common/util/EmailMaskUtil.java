package toy.recipit.common.util;

public class EmailMaskUtil {
    public static String emailMasking(String email) {

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
