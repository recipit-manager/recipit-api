package toy.recipit.controller.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class NoticeRequestDto {
    private final List<NoticeItemDto> noticeItems;
}
