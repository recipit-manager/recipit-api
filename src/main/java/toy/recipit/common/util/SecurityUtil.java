package toy.recipit.common.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    @Value("${CRYPTO_PASSWORD}")
    private String cryptoPassword;
    private AES256TextEncryptor encryptor;
    private final String PACK_SEPARATOR = ":";

    @PostConstruct
    void init() {
        encryptor = new AES256TextEncryptor();
        encryptor.setPassword(cryptoPassword);
    }

    private Optional<String> encrypt(String plainText) {
        if (plainText == null) return Optional.empty();
        return Optional.of(encryptor.encrypt(plainText));
    }

    private Optional<String> decrypt(String cipherText) {
        if (cipherText == null) return Optional.empty();
        return Optional.of(encryptor.decrypt(cipherText));
    }

    private Optional<String> createHasing(String plainText) {
        if (plainText == null) return Optional.empty();
        return Optional.of(DigestUtils.sha256Hex(plainText));
    }

    public Optional<String> encryptWithHasing(String normalizedPlainText) {
        if (normalizedPlainText == null) return Optional.empty();
        return createHasing(normalizedPlainText).flatMap(token ->
                encrypt(normalizedPlainText).map(cipherText ->
                        token + PACK_SEPARATOR + cipherText
                )
        );
    }

    public Optional<String> decryptFromPacked(String packedValue) {
        if (packedValue == null) {
            return Optional.empty();
        }
        int separatorIndex = packedValue.indexOf(PACK_SEPARATOR);
        String cipherText = (separatorIndex >= 0)
                ? packedValue.substring(separatorIndex + 1)
                : packedValue;
        return decrypt(cipherText);
    }

    public Optional<String> extractHasing(String packedValue) {
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
