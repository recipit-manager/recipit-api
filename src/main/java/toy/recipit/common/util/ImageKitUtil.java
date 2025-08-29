package toy.recipit.common.util;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ImageKitUtil {
    @Value("${imagekit.publicKey}")
    private String publicKey;

    @Value("${imagekit.privateKey}")
    private String privateKey;

    @Value("${imagekit.urlEndpoint}")
    private String urlEndpoint;

    @Value("${imagekit.defaultExpireTime:60}")
    private int defaultExpireSeconds;

    private final ImageKit imageKit = ImageKit.getInstance();

    @PostConstruct
    private void init() {
        imageKit.setConfig(new io.imagekit.sdk.config.Configuration(publicKey, privateKey, urlEndpoint));
    }

    public String upload(MultipartFile file) throws Exception {
        FileCreateRequest fileCreateRequest = new FileCreateRequest(file.getBytes(), String.valueOf(System.currentTimeMillis()));
        fileCreateRequest.setPrivateFile(true);
        fileCreateRequest.setUseUniqueFileName(true);

        return imageKit.upload(fileCreateRequest).getFilePath();
    }

    public Optional<String> getUrl(String filePath) {
        return getUrl(filePath, defaultExpireSeconds);
    }

    public Optional<String> getUrl(String filePath, int expireSeconds) {
        String url = imageKit.getUrl(Map.of(
                "path", filePath,
                "signed", true,
                "expireSeconds", expireSeconds));

        return url == null
                ? Optional.empty()
                : Optional.of(url);
    }
}