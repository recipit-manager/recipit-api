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
    }

    public interface GroupCode {
        @Getter
        @RequiredArgsConstructor
        enum Language {
            KO("CT100"),
            EN("CT200");

            private final String groupCode;
        }

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
        String CHAR_POOL_WITHOUT_SIMILAR = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";
    }

    public interface System {
        String SYSTEM_NUMBER = "0";
    }
}
