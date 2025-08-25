package toy.recipit.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toy.recipit.common.ApiResult;
import toy.recipit.dto.ApiResponse;

import java.util.stream.Collectors;

import static toy.recipit.common.Constant.*;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    // DB 오류
    @ExceptionHandler({CannotGetJdbcConnectionException.class, RedisConnectionFailureException.class})
    public ResponseEntity<ApiResponse<String>> handleDbConnection(Exception e, HttpServletRequest req) {
        log.error("{} {} {}",TAG_DB_FAIL , req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(
                new ApiResponse<>(ApiResult.DB_CONNECT_FAIL.getCode(), ApiResult.DB_CONNECT_FAIL.getMessage(), null)
        );
    }

    // 잘못된 요청
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleValidation(ConstraintViolationException e, HttpServletRequest req) {
        String details = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(" / "));

        log.warn("{} {} - {}",TAG_VALIDATION , req.getMethod(), req.getRequestURI(), e);

        String finalMessage = ApiResult.BAD_REQUEST.getMessage() + " (" + details + ")";

        return ResponseEntity.ok(
                new ApiResponse<>(ApiResult.BAD_REQUEST.getCode(), finalMessage, null)
        );
    }

    // 그 외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e, HttpServletRequest req) {
        log.error("{} {} {}",TAG_SERVER_ERROR, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(
                new ApiResponse<>(ApiResult.SERVER_ERROR.getCode(), ApiResult.SERVER_ERROR.getMessage(), null)
        );
    }
}
