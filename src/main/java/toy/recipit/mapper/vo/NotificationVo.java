package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationVo {
    final private String noticeNo;
    final private String userNo;
    final private String recipeNo;
    final private String categoryCode;
    final private String categoryCodeName;
    final private String readYn;
    final private String contents;
    final private String createDateTime;
}
