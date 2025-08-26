package toy.recipit.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final String code;
    private final String message;
    private final T data;

    @Getter
    public enum Result {
        SUCCESS("0000", "api.success"),
        BAD_REQUEST("1001", "api.error.bad_request"),
        ARGUMENT_ERROR("1002", "api.error.argument"),
        SERVER_ERROR("4001", "api.error.server"),
        DB_CONNECT_FAIL("4999", "api.error.db");

        private final String code;
        private final String messageKey;

        Result(String code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }
    }
}





