package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "재료 유형 정보")
public class IngredientTypeDto {
    @Schema(description = "카테고리 코드", example = "I01")
    private final String categoryCode;
    @Schema(description = "카테고리명", example = "주재료")
    private final String categoryName;
}
