package toy.recipit.common.util;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String encrypt(
            @NonNull
            String plainText
    ) {
        return encryptor.encrypt(plainText);
    }

    public String decrypt(
            @NonNull
            String encryptedText
    ) {
        return encryptor.decrypt(encryptedText);
    }

}
