package toy.recipit.controller.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoticeItemDto {
    private final String noticeNo;
    private final String userNo;
}
