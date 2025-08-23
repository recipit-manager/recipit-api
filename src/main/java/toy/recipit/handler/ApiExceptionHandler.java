package toy.recipit.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toy.recipit.dto.ApiError;
import toy.recipit.dto.ApiResponse;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private void logError(String tag, Exception e, HttpServletRequest req) {
        log.error("[{}] {} {} - {}",
                tag,
                req.getMethod(),
                req.getRequestURI(),
                e.getMessage()
        );
    }

    // DB 오류
    @ExceptionHandler({CannotGetJdbcConnectionException.class, RedisConnectionFailureException.class})
    public ResponseEntity<ApiResponse<String>> handleDbConnection(Exception e, HttpServletRequest req) {
        logError("DB_FAIL", e, req);

        return ResponseEntity.ok(
                new ApiResponse<>(ApiError.DB_CN_FAIL.code(), ApiError.DB_CN_FAIL.message(), null)
        );
    }

    // 잘못된 요청
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleValidation(ConstraintViolationException e) {
        String details = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(" / "));

        String finalErrorMessage = ApiError.BAD_REQUEST.format(Map.of("details", details));

        return ResponseEntity.ok(
                new ApiResponse<>(ApiError.BAD_REQUEST.code(), finalErrorMessage, null)
        );
    }

    // 그 외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e, HttpServletRequest req) {
        logError("SERVER_ERROR", e, req);

        return ResponseEntity.ok(
                new ApiResponse<>(ApiError.SERVER_ERROR.code(), ApiError.SERVER_ERROR.message(), null)
        );
    }
}
