package toy.recipit.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import toy.recipit.controller.factory.ApiResponseFactory;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.ApiResponse;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
    private final MessageSource messageSource;
    private final ApiResponseFactory apiResponseFactory;

    @ExceptionHandler({CannotGetJdbcConnectionException.class, RedisConnectionFailureException.class})
    public ResponseEntity<ApiResponse<String>> handleDbConnection(Exception e, HttpServletRequest req) {
        log.error("{} {} - {}", Constants.LogTag.DB_FAIL, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.DB_CONNECT_FAIL));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleValidation(ConstraintViolationException e, HttpServletRequest req) {
        String details = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .map(code -> messageSource.getMessage(code, null, LocaleContextHolder.getLocale()))
                .collect(Collectors.joining(" / "));

        log.warn("{} {} - {}", Constants.LogTag.VALIDATION, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.BAD_REQUEST, details));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleArgument(MethodArgumentTypeMismatchException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.ARGUMENT, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.ARGUMENT_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e, HttpServletRequest req) {
        log.error("{} {} - {}", Constants.LogTag.SERVER_ERROR, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.SERVER_ERROR));
    }
}
