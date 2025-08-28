package toy.recipit.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
        @RequiredArgsConstructor
        enum Language {
            KO("CT100"),
            EN("CT200");

            private final String groupCode;
        }

        String EMAIL_DOMAIN    = "AC400";
        String RECIPE_CATEGORY = "RC100";
        String INGREDIENT_TYPE = "RC300";
        String REPORT_CATEGORY = "RC400";
        String DIFFICULTY      = "RC320";

        @Getter
        @RequiredArgsConstructor
        enum RefriItem {
            VEGETABLE("RI100", "채소"),
            FRUIT("RI110", "과일"),
            MEAT("RI120", "육류"),
            SEAFOOD("RI130", "해산물"),
            SEASONING("RI140", "조미료"),
            PROCESSED("RI150", "가공/유제품");

            private final String code;
            private final String name;
        }
    }
}
