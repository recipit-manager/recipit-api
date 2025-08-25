package toy.recipit.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toy.recipit.common.ApiResult;
import toy.recipit.common.Constants;
import toy.recipit.common.MessageProvider;
import toy.recipit.dto.ApiResponse;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
    private final MessageProvider messageProvider;

    // DB 오류
    @ExceptionHandler({CannotGetJdbcConnectionException.class, RedisConnectionFailureException.class})
    public ResponseEntity<ApiResponse<String>> handleDbConnection(Exception e, HttpServletRequest req) {
        log.error("{} {} - {}", Constants.LogTag.DB_FAIL, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        ApiResult.DB_CONNECT_FAIL.getCode(),
                        messageProvider.getMessage(ApiResult.DB_CONNECT_FAIL.getMessageKey()),
                        null
                )
        );
    }

    // 잘못된 요청
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleValidation(ConstraintViolationException e, HttpServletRequest req) {
        String details = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .map(messageProvider::getMessage)
                .collect(Collectors.joining(" / "));

        log.warn("{} {} - {}", Constants.LogTag.VALIDATION, req.getMethod(), req.getRequestURI(), e);

        String finalMessage = messageProvider.getMessage(
                ApiResult.BAD_REQUEST.getMessageKey(),
                details
        );

        return ResponseEntity.ok(
                new ApiResponse<>(ApiResult.BAD_REQUEST.getCode(), finalMessage, null)
        );
    }

    // 그 외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e, HttpServletRequest req) {
        log.error("{} {} - {}", Constants.LogTag.SERVER_ERROR, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        ApiResult.SERVER_ERROR.getCode(),
                        messageProvider.getMessage(ApiResult.SERVER_ERROR.getMessageKey()),
                        null
                )
        );
    }
}
