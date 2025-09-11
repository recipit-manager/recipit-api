package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AutoLoginResult {
    private final String userNickname;
    private final String userNo;
    private final String autoLoginToken;
}
