package toy.recipit.common;

import lombok.Getter;

@Getter
public enum Language {
    KO(Constants.GroupCode.LANGUAGE_KO),
    EN(Constants.GroupCode.LANGUAGE_EN);

    private final String groupCode;

    Language(String groupCode) {
        this.groupCode = groupCode;
    }
}