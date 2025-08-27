package toy.recipit.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class BaseUrlProvider {
    public String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();
    }
}
