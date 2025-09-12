package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResultDto {
    private final String userNo;
    private final String userNickname;
    private final String userStatusCode;
    private final String autoLoginToken;
}
