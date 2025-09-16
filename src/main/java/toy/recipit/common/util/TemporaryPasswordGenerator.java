package toy.recipit.common.util;

import toy.recipit.common.Constants;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TemporaryPasswordGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String passwordGenerate() {
        int length = RANDOM.nextInt((Constants.Password.MAX_LENGTH - Constants.Password.MIN_LENGTH) + 1) + Constants.Password.MIN_LENGTH;

        List<Character> temporaryPassword = new ArrayList<>();

        addRandomChar(temporaryPassword, Constants.Password.UPPERCASE_LETTERS);
        addRandomChar(temporaryPassword, Constants.Password.LOWERCASE_LETTERS);
        addRandomChar(temporaryPassword, Constants.Password.DIGITS);
        addRandomChar(temporaryPassword, Constants.Password.SPECIAL_CHARACTERS);

        for (int i = temporaryPassword.size(); i < length; i++) {
            temporaryPassword.add(Constants.Password.ALL_ALLOWED_CHARACTERS.charAt(RANDOM.nextInt(Constants.Password.ALL_ALLOWED_CHARACTERS.length())));
        }

        Collections.shuffle(temporaryPassword, RANDOM);

        StringBuilder sb = new StringBuilder();
        for (char c : temporaryPassword) {
            sb.append(c);
        }

        return sb.toString();
    }

    private static void addRandomChar(List<Character> temporaryPassword, String charSetPool) {
        char nextChar;
        do {
            nextChar = charSetPool.charAt(RANDOM.nextInt(charSetPool.length()));
        } while (temporaryPassword.size() >= 2 &&
                temporaryPassword.get(temporaryPassword.size() - 1) == nextChar &&
                temporaryPassword.get(temporaryPassword.size() - 2) == nextChar);
        temporaryPassword.add(nextChar);
    }
}