package toy.recipit.common.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    @Value("${JASYPT_PASSWORD}")
    private String cryptoPassword;
    private AES256TextEncryptor encryptor;

    @PostConstruct
    void init() {
        encryptor = new AES256TextEncryptor();
        encryptor.setPassword(cryptoPassword);
    }

    public Optional<String> encrypt(
            @NotNull
            String plainText
    ) {
        return Optional.of(encryptor.encrypt(plainText));
    }

    public Optional<String> decrypt(
            @NotNull
            String encryptedText
    ) {
        return Optional.of(encryptor.decrypt(encryptedText));
    }

}
