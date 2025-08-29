package toy.recipit.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

        String EMAIL_DOMAIN    = "AC400";
        String RECIPE_CATEGORY = "RC100";
        String INGREDIENT_TYPE = "RC300";
        String REPORT_CATEGORY = "RC400";
        String DIFFICULTY      = "RC320";

        String REFRI_VEGETABLE = "RI100";
        String REFRI_FRUIT     = "RI110";
        String REFRI_MEAT      = "RI120";
        String REFRI_SEAFOOD   = "RI130";
        String REFRI_SEASONING = "RI140";
        String REFRI_PROCESSED = "RI150";

        List<String> REFRI_ALL_CODES = List.of(
                REFRI_VEGETABLE,
                REFRI_FRUIT,
                REFRI_MEAT,
                REFRI_SEAFOOD,
                REFRI_SEASONING,
                REFRI_PROCESSED
        );
    }
}
