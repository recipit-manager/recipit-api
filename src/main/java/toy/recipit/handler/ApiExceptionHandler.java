package toy.recipit.handler;

import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import toy.recipit.common.Constants;
import toy.recipit.common.exception.IngredientNotFoundException;
import toy.recipit.common.exception.NotLoginStatusException;
import toy.recipit.common.exception.SessionNotExistsException;
import toy.recipit.common.exception.UserNotFoundException;
import toy.recipit.common.exception.UserStatusInactiveException;
import toy.recipit.common.exception.UserStatusLockException;
import toy.recipit.common.exception.loginFailException;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleArgumentValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.ARGUMENT, req.getMethod(), req.getRequestURI(), e);

        String details = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.BAD_REQUEST, details));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<String>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.ARGUMENT, req.getMethod(), req.getRequestURI(), e);

        String details = messageSource.getMessage("image.large.size", null, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.BAD_REQUEST, details));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingServletRequestPart(MissingServletRequestPartException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.ARGUMENT, req.getMethod(), req.getRequestURI(), e);

        String details = StringUtils.EMPTY;

        if (Constants.ImageParameterName.MAIN_IMAGE.equals(e.getRequestPartName())) {
            details = messageSource.getMessage("validation.recipe.mainImage.null", null, LocaleContextHolder.getLocale());
        }
        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.BAD_REQUEST, details));
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleIngredientNotFound(IngredientNotFoundException e, HttpServletRequest req) {
        log.error("{} {} - {}", Constants.LogTag.DATA_MISSING, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.DATA_MISSING));
    }

    @ExceptionHandler(loginFailException.class)
    public ResponseEntity<ApiResponse<String>> handleLoginFailException(loginFailException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.ARGUMENT, req.getMethod(), req.getRequestURI(), e);

        String details = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.BAD_REQUEST, details));
    }

    @ExceptionHandler(SessionNotExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleSessionNotExistsException(SessionNotExistsException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.SESSION_ERROR, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.SESSION_NOT_FOUND));
    }

    @ExceptionHandler(NotLoginStatusException.class)
    public ResponseEntity<ApiResponse<String>> handleNotLoginStatusException(NotLoginStatusException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.LOGIN_STATUS_ERROR, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.NOT_LOGIN_STATUS));
    }

    @ExceptionHandler(UserStatusLockException.class)
    public ResponseEntity<ApiResponse<String>> handleUserStatusLockException(UserStatusLockException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.LOGIN_STATUS_ERROR, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.LOCK_LOGIN_STATUS));
    }

    @ExceptionHandler(UserStatusInactiveException.class)
    public ResponseEntity<ApiResponse<String>> handleUserStatusInactiveException(UserStatusInactiveException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.LOGIN_STATUS_ERROR, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.INACTIVE_LOGIN_STATUS));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFound(UserNotFoundException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.DATA_MISSING, req.getMethod(), req.getRequestURI(), e);

        String details = messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.BAD_REQUEST, details));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingServletRequestParameter(MissingServletRequestParameterException e, HttpServletRequest req) {
        log.warn("{} {} - {}", Constants.LogTag.ARGUMENT, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.ARGUMENT_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e, HttpServletRequest req) {
        log.error("{} {} - {}", Constants.LogTag.SERVER_ERROR, req.getMethod(), req.getRequestURI(), e);

        return ResponseEntity.ok(apiResponseFactory.error(ApiResponse.Result.SERVER_ERROR));
    }
}
