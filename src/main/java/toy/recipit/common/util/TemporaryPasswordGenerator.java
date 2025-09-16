package toy.recipit.common.util;

import toy.recipit.common.Constants;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TemporaryPasswordGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String PasswordGenerate() {
        int length = RANDOM.nextInt((Constants.Password.MAX_LENGTH - Constants.Password.MIN_LENGTH) + 1) + Constants.Password.MIN_LENGTH;

        List<Character> chars = new ArrayList<>();

        chars.add(Constants.Password.UPPERCASE_LETTERS.charAt(RANDOM.nextInt(Constants.Password.UPPERCASE_LETTERS.length())));
        chars.add(Constants.Password.LOWERCASE_LETTERS.charAt(RANDOM.nextInt(Constants.Password.LOWERCASE_LETTERS.length())));
        chars.add(Constants.Password.DIGITS.charAt(RANDOM.nextInt(Constants.Password.DIGITS.length())));
        chars.add(Constants.Password.SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(Constants.Password.SPECIAL_CHARACTERS.length())));

        for (int i = chars.size(); i < length; i++) {
            chars.add(Constants.Password.ALL_ALLOWED_CHARACTERS.charAt(RANDOM.nextInt(Constants.Password.ALL_ALLOWED_CHARACTERS.length())));
        }

        Collections.shuffle(chars, RANDOM);

        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }

        String password = sb.toString();

        if (hasTripleRepeat(password)) {
            return PasswordGenerate();
        }

        return password;
    }

    private static boolean hasTripleRepeat(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) &&
                    password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
}