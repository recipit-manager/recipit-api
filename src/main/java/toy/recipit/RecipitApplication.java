package toy.recipit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import java.time.ZoneOffset;
import java.util.TimeZone;

@EnableRedisHttpSession
@SpringBootApplication
public class RecipitApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
        SpringApplication.run(RecipitApplication.class, args);
    }

}
