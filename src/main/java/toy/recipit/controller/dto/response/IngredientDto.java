package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "재료 정보")
public class IngredientDto {
    @Schema(description = "재료명", example = "돼지고기")
    private final String name;
    @Schema(description = "재료 카테고리 코드", example = "I01")
    private final String categoryCode;
    @Schema(description = "재료 카테고리 명", example = "주재료")
    private final String categoryName;
    @Schema(description = "재료 수량", example = "3")
    private final String quantity;
    @Schema(description = "수량 단위", example = "kg")
    private final String unit;
    @Schema(description = "재료 TIP", example = "고기를 30분 정도 재워둡니다.")
    private final String tip;
}