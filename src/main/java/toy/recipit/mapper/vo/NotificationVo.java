package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationVo {
    private String noticeNo;
    private String userNo;
    private String recipeNo;
    private String categoryCode;
    private String readYn;
    private String contents;
    private String createDateTime;
}
