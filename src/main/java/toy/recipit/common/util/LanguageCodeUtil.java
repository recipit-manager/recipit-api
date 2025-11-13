package toy.recipit.common.util;

import toy.recipit.common.Constants;

public class LanguageCodeUtil {
    public static String toGroupCode(String acceptLanguage) {
        if (acceptLanguage.equals(Constants.LanguageCode.EN)) {
            return Constants.GroupCode.Language.EN.getGroupCode();
        }

        return Constants.GroupCode.Language.KO.getGroupCode();
    }
}
