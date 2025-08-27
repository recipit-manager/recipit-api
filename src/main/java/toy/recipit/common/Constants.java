package toy.recipit.common;

import lombok.Getter;

public final class Constants {

    public interface Yn {
        String YES = "Y";
        String NO  = "N";
    }

    public interface LogTag {
        String SERVER_ERROR = "SERVER_ERROR";
        String DB_FAIL      = "DB_FAIL";
        String VALIDATION   = "VALIDATION";
        String ARGUMENT     = "ARGUMENT";
    }

    public interface GroupCode {
        @Getter
        enum Language {
            KO("CT100"),
            EN("CT200");

            private final String groupCode;

            Language(String groupCode) {
                this.groupCode = groupCode;
            }
        }
        String EMAIL_DOMAIN = "AC400";
    }
}
