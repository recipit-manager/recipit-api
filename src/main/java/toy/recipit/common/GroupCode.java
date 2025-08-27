package toy.recipit.common;

import lombok.Getter;

@Getter
public enum GroupCode {
    KO(Constants.GroupCode.LANGUAGE_KO),
    EN(Constants.GroupCode.LANGUAGE_EN);

    private final String groupCode;

    GroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}