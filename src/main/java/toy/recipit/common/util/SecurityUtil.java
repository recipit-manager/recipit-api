package toy.recipit.common.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    @Value("${CRYPTO_BASE64KEY}")
    private String base64EncryptionKey;

    private SecretKeySpec encryptionKeySpec;
    private final SecureRandom secureRandom = new SecureRandom();

    private static final int GCM_TAG_BIT_LENGTH = 128;
    private static final int GCM_IV_BYTE_LENGTH = 12;
    private static final String PACK_SEPARATOR = ":";

    @PostConstruct
    void init() {
        encryptionKeySpec = new SecretKeySpec(Base64.getDecoder().decode(base64EncryptionKey), "AES");
    }

    private String encrypt(String plainText) {
        if (plainText == null) return null;
        try {
            byte[] initializationVector = new byte[GCM_IV_BYTE_LENGTH];
            secureRandom.nextBytes(initializationVector);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKeySpec, new GCMParameterSpec(GCM_TAG_BIT_LENGTH, initializationVector));

            byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] outputBytes = ByteBuffer
                    .allocate(initializationVector.length + cipherBytes.length)
                    .put(initializationVector)
                    .put(cipherBytes)
                    .array();

            return Base64.getEncoder().encodeToString(outputBytes);
        } catch (Exception exception) {
            throw new IllegalStateException("AES encrypt error", exception);
        }
    }

    private String decrypt(String cipherBase64Text) {
        if (cipherBase64Text == null) return null;
        try {
            byte[] allBytes = Base64.getDecoder().decode(cipherBase64Text);
            ByteBuffer byteBuffer = ByteBuffer.wrap(allBytes);

            byte[] initializationVector = new byte[GCM_IV_BYTE_LENGTH];
            byteBuffer.get(initializationVector);

            byte[] cipherBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherBytes);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, encryptionKeySpec, new GCMParameterSpec(GCM_TAG_BIT_LENGTH, initializationVector));

            byte[] plainBytes = cipher.doFinal(cipherBytes);
            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new IllegalStateException("AES decrypt error", exception);
        }
    }

    private String createToken(String normalizedPlainText) {
        return DigestUtils.sha256Hex(normalizedPlainText);
    }

    public String encryptWithToken(String normalizedPlainText) {
        if (normalizedPlainText == null) return null;
        String token = createToken(normalizedPlainText);
        String cipherBase64Text = encrypt(normalizedPlainText);
        return token + PACK_SEPARATOR + cipherBase64Text;
    }

    public Optional<String> decryptFromPacked(String packedValue) {
        if (packedValue == null) {
            return Optional.empty();
        }
        int separatorIndex = packedValue.indexOf(PACK_SEPARATOR);
        String cipherBase64Text = (separatorIndex >= 0) ? packedValue.substring(separatorIndex + 1) : packedValue;
        return Optional.ofNullable(decrypt(cipherBase64Text));
    }

    public Optional<String> extractToken(String packedValue) {
        if (packedValue == null) {
            return Optional.empty();
        }
        int separatorIndex = packedValue.indexOf(PACK_SEPARATOR);
        if (separatorIndex < 0) {
            return Optional.empty();
        }
        return Optional.of(packedValue.substring(0, separatorIndex));
    }
}