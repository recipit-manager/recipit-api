package toy.recipit.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.api-docs.version}") String appVersion) {
        Info info = new Info()
                .title("Recipit API")
                .version(appVersion)
                .description("Recipit 서비스 제공을 위한 API 정보 입니다.")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                             .name("recipit manager")
                             .url("https://www.pionnet.co.kr/")
                             .email("recipit.manager@gmail.com"))
                .license(new License()
                            .name("Apache License Version 2.0")
                            .url("http://www.apache.org/licenses/LICENSE-2.0")
                );

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
