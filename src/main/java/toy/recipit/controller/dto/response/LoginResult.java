package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResult {
    private final SessionUser sessionUser;
    private final String autoLoginToken;
}
