package toy.recipit.common;

public enum ApiResult {
    SUCCESS("0000", "api.success"),
    BAD_REQUEST("1001", "api.error.bad_request"),
    SERVER_ERROR("4001", "api.error.server"),
    DB_CONNECT_FAIL("4999", "api.error.db");

    private final String code;
    private final String messageKey;

    ApiResult(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public String getCode() {

        return code;
    }

    public String getMessageKey() {

        return messageKey;
    }
}
