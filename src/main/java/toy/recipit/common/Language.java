package toy.recipit.common;

import lombok.Getter;

@Getter
public enum Language {
    KO("CT100"),
    EN("CT200");

    private final String groupCode;

    Language(String groupCode) {
        this.groupCode = groupCode;
    }
}