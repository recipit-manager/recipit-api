package toy.recipit.Handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toy.recipit.dto.ApiResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

    //DB오류
    @ExceptionHandler({CannotGetJdbcConnectionException.class, RedisConnectionFailureException.class})
    public ResponseEntity<ApiResponse<String>> handleDbConnection() {

        return ResponseEntity.ok(new ApiResponse<>("4999", "데이터베이스 연결에 실패하였습니다.", null));
    }

    //잘못된 요청
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiResponse<String>> handleValidation() {

        return ResponseEntity.ok(new ApiResponse<>("1001", "잘못된 요청입니다.", null));
    }

    //그 외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException() {

        return ResponseEntity.badRequest().body(new ApiResponse<>("4001", "서버에 오류가 발생하였습니다.", null));
    }
}
