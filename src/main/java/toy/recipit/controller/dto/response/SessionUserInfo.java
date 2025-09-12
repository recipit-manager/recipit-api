package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class SessionUserInfo implements Serializable {
    private final String userNo;
    private final String userNickname;
    private final String userStatusCode;
}
