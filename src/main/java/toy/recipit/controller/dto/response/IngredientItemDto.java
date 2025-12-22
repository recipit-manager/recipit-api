package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "재료 정보")
public class IngredientItemDto {
    @Schema(description = "재료명", example = "양상추")
    private final String name;
    @Schema(description = "아이콘 경로", example = "/images/ingredient/I01/01.png")
    private final String iconUrl;
}
