package toy.recipit.common.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashingUtil {
    public String sha256Hashing(String inputText) {
        byte[] sha256DigestBytes = textSha256Hashing(inputText);
        return bytesToHex(sha256DigestBytes);
    }

    private byte[] textSha256Hashing(String inputText) {
        try {
            MessageDigest sha256MessageDigest = MessageDigest.getInstance("SHA-256");
            sha256MessageDigest.reset();
            return sha256MessageDigest.digest(inputText.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private String bytesToHex(byte[] inputBytes) {
        StringBuilder hexStringBuilder = new StringBuilder(inputBytes.length * 2);
        for (byte singleByte : inputBytes) {
            hexStringBuilder.append(String.format("%02x", singleByte));
        }
        return hexStringBuilder.toString();
    }
}
