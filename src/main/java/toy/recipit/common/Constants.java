package toy.recipit.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public final class Constants {
    public interface Yn {
        String YES = "Y";
        String NO = "N";
    }

    public interface LogTag {
        String SERVER_ERROR = "SERVER_ERROR";
        String DB_FAIL = "DB_FAIL";
        String VALIDATION = "VALIDATION";
        String ARGUMENT = "ARGUMENT";
        String DATA_MISSING = "DATA_MISSING";
        String SESSION_ERROR = "SESSION_ERROR";
        String LOGIN_STATUS_ERROR = "LOGIN_STATUS_ERROR";
    }

    public interface GroupCode {
        @Getter
        @RequiredArgsConstructor
        enum Language {
            KO("CT100"),
            EN("CT200");

            private final String groupCode;
        }

        String NOTIFICATION = "AC300";
        String EMAIL_DOMAIN = "AC400";
        String RECIPE_CATEGORY = "RC100";
        String INGREDIENT_TYPE = "RC300";
        String REPORT_CATEGORY = "RC400";
        String DIFFICULTY = "RC320";

        enum RefriIngredientCategory {
            RI100,  // 채소
            RI110,  // 과일
            RI120,  // 육류
            RI130,  // 해산물
            RI140,  // 조미료
            RI150;  // 가공/유제품
        }
    }

    public interface EmailVerification {
        String ACTIVATE = "V100";
        String SUCCESS = "V200";
    }

    public interface RandomCode {
        String UPPERCASE_ALPHABET_NUMBER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    }

    public interface SystemId {
        String SYSTEM_NUMBER = "0";
    }

    public interface UserStatus {
        String ACTIVE = "A100";
        String INACTIVE = "A200";
        String STOP = "A300";
    }

    public interface UserLogin {
        int LOGIN_FAIL_COUNT_INITIAL = 0;
        int AUTO_LOGIN_EXPIRATION_DAYS = 7;
        String AUTO_LOGIN_COOKIE_NAME = "AUTO_LOGIN_TOKEN";
        int LOGIN_FAIL_INACTIVE_THRESHOLD = 5;
    }

    public interface SessionKey {
        String USER = "user";
    }

    public interface Password {
        String UPPERCASE_LETTERS   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LOWERCASE_LETTERS   = "abcdefghijklmnopqrstuvwxyz";
        String DIGITS              = "0123456789";
        String SPECIAL_CHARACTERS  = "!@#$%*";
        String ALL_ALLOWED_CHARACTERS = UPPERCASE_LETTERS + LOWERCASE_LETTERS + DIGITS + SPECIAL_CHARACTERS;
        int MIN_LENGTH = 8;
        int MAX_LENGTH = 16;
    }

    public interface image{
        String THUMBNAIL = "IM01";
        String STEP = "IM02";
        String COMPLETE = "IM03";
    }
}
