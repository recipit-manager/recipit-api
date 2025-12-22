package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "알림 정보")
public class NoticeItemDto {
    @Schema(description = "알림 번호", example = "1001")
    private final String noticeNo;
    @Schema(description = "알림을 수신한 사용자 번호", example = "1001")
    private final String userNo;
}
