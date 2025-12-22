package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "새로운 알림 목록 정보")
public class NoticeRequestDto {
    @Schema(description = "새로운 알림 목록")
    private final List<NoticeItemDto> noticeItems;
}
