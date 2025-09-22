package toy.recipit.common.util;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.HttpURLConnection;
import java.net.URL;
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

    public String getUrl(String filePath) {
        return getUrl(filePath, defaultExpireSeconds);
    }

    public String getUrl(String filePath, int expireSeconds) {
        String url = imageKit.getUrl(Map.of(
                "path", filePath,
                "signed", true,
                "expireSeconds", expireSeconds));

        if(url==null || !existsByUrl(url)) {
            throw new RuntimeException("잘못된 경로입니다." + filePath);
        }

        return url;
    }

    private boolean existsByUrl(String signedUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(signedUrl).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
}