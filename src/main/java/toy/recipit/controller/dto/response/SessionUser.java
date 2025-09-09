package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class SessionUser implements Serializable {
    private final String userNo;
    private final String nickName;
    private final String statusCode;
}