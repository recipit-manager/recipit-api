package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "알림 정보")
public class NotificationDto {
    @Schema(description = "알림 번호", example = "1034")
    private final String id;
    @Schema(description = "알린 내용", example = "작성 중이던 임시저장 레시피가 1일 후 만료됩니다.")
    private final String contents;
    @Schema(description = "관련 레시피 번호", example = "1001")
    private final String recipeNo;
    @Schema(description = "알림 유형", example = "NT01")
    private final CommonCodeAndNameDto notificationType;
    @Schema(description = "읽음 여부", example = "N")
    private final String readYn;
    @Schema(description = "알림 수신 시간", example = "2021-03-23T12:00:00")
    private final String receivedTime;
}
