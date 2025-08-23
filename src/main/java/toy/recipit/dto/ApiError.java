package toy.recipit.dto;

import java.util.Map;

public enum ApiError {
    SUCCESS("0000", "성공"),
    BAD_REQUEST("1001", "잘못된 요청입니다: {details}"),
    SERVER_ERROR("4001", "서버에 오류가 발생하였습니다."),
    DB_CN_FAIL("4999", "데이터베이스 연결에 실패하였습니다.");

    private final String code;
    private final String message;

    ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() { return code; }
    public String message() { return message; }
    public String format(Map<String, String> params) {
        String formatted = message;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formatted = formatted.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return formatted;
    }
}