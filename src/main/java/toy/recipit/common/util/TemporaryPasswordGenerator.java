package toy.recipit.common.util;

import toy.recipit.common.Constants;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class TemporaryPasswordGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String passwordGenerate() {
        int length = RANDOM.nextInt((Constants.Password.MAX_LENGTH - Constants.Password.MIN_LENGTH) + 1) + Constants.Password.MIN_LENGTH;

        List<Character> temporaryPassword = new ArrayList<>();

        temporaryPassword.add(getRandomChar(Constants.Password.UPPERCASE_LETTERS, temporaryPassword));
        temporaryPassword.add(getRandomChar(Constants.Password.LOWERCASE_LETTERS, temporaryPassword));
        temporaryPassword.add(getRandomChar(Constants.Password.DIGITS, temporaryPassword));
        temporaryPassword.add(getRandomChar(Constants.Password.SPECIAL_CHARACTERS, temporaryPassword));

        for (int i = temporaryPassword.size(); i < length; i++) {
            temporaryPassword.add(Constants.Password.ALL_ALLOWED_CHARACTERS
                    .charAt(RANDOM.nextInt(Constants.Password.ALL_ALLOWED_CHARACTERS.length())));
        }

        StringBuilder sb = new StringBuilder();
        for (char c : temporaryPassword) {
            sb.append(c);
        }

        return sb.toString();
    }

    private static char getRandomChar(String charSetPool, List<Character> currentPassword) {
        char nextChar;
        do {
            nextChar = charSetPool.charAt(RANDOM.nextInt(charSetPool.length()));
        } while (currentPassword.size() >= 2 &&
                currentPassword.get(currentPassword.size() - 1) == nextChar &&
                currentPassword.get(currentPassword.size() - 2) == nextChar);
        return nextChar;
    }
}