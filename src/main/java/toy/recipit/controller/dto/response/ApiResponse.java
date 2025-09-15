package toy.recipit.controller.dto.response;

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
        LOCK_LOGIN_STATUS("0001", "api.error.lock_login_status"),
        INACTIVE_LOGIN_STATUS("0002", "api.error.inactive_login_status"),
        BAD_REQUEST("1001", "api.error.bad_request"),
        ARGUMENT_ERROR("1002", "api.error.argument"),
        SESSION_NOT_FOUND("3001", "api.error.session"),
        NOT_LOGIN_STATUS("3002", "api.error.not_login_status"),
        SERVER_ERROR("4001", "api.error.server"),
        DATA_MISSING("4002", "api.error.data_missing"),
        DB_CONNECT_FAIL("4999", "api.error.db");

        private final String code;
        private final String messageKey;

        Result(String code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }
    }
}





