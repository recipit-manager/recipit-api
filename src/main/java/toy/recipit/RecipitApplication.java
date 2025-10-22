package toy.recipit;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.TimeZone;

@Slf4j
@EnableAsync
@EnableRedisHttpSession
@SpringBootApplication
public class RecipitApplication {
    private final DataSource dataSource;

    public RecipitApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() throws SQLException {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));

        try (Connection connection = dataSource.getConnection()) {
            log.info("Database[{}] connection established", connection.getMetaData().getDatabaseProductName());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(RecipitApplication.class, args);
    }
}
