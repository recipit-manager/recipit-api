package toy.recipit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    public ApiResponse() {

    }

    //성공(0000)
    public ApiResponse<T> success(T data) {
        this.code = "0000";
        this.message = "성공.";
        this.data = data;
        return this;
    }

    //잘못된 요청(1001)
    public ApiResponse<T> badRequest() {
        this.code = "1001";
        this.message = "잘못된 요청입니다.";
        this.data = null;
        return this;
    }

    //서버 오류(4001)
    public ApiResponse<T> serverError() {
        this.code = "4001";
        this.message = "서버에 오류가 발생하였습니다.";
        this.data = null;
        return this;
    }

    //DB 연결 실패(4999)
    public ApiResponse<T> dbConnectionFailed() {
        this.code = "4999";
        this.message = "데이터베이스 연결에 실패하였습니다.";
        this.data = null;
        return this;
    }
}





