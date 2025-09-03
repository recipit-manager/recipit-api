package toy.recipit.common.util;

import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.DigestUtils;

@Component
public class HashingUtil {
    public String sha256Hashing(String inputText) {
        return DigestUtils.sha256Hex(inputText);
    }
}
