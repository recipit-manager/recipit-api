package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public class AutoLoginResultDto {
    private final boolean needDeleteToken;
    private final String userNickname;
    private final String userNo;
    private final String userStatusCode;
    private final String autoLoginToken;

    public AutoLoginResultDto(boolean needDeleteToken) {
        this.needDeleteToken = needDeleteToken;
        this.userNickname = StringUtils.EMPTY;
        this.userNo = StringUtils.EMPTY;
        this.userStatusCode = StringUtils.EMPTY;
        this.autoLoginToken = StringUtils.EMPTY;
    }
}
