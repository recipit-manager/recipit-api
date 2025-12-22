package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Schema(description = "임시저장 레시피 재료 정보")
public class DraftIngredientDto {
    @Size(max = 10, message = "validation.recipe.ingredient.name.size")
    @Schema(description = "재료명", example = "돼지고기")
    private final String name;

    @Schema(description = "재료 카테고리 코드", example = "I01")
    private final String categoryCode;

    @Digits(integer = 5, fraction = 0, message = "validation.recipe.ingredient.quantity.size")
    @Schema(description = "수량", example = "1")
    private final Integer quantity;

    @Size(max = 5, message = "validation.recipe.ingredient.unit.size")
    @Schema(description = "단위", example = "g")
    private final String unit;

    @Size(max = 30, message = "validation.recipe.ingredient.tip.size")
    @Schema(description = "재료tip", example = "돼지고기는 실온에서 냉기를 빼주세요.")
    private final String tip;

    public DraftIngredientDto(String name, String categoryCode, Integer quantity, String unit, String tip) {
        this.name = (name == null ? StringUtils.EMPTY : name);
        this.categoryCode = (categoryCode == null ? StringUtils.EMPTY : categoryCode);
        this.quantity = (quantity == null ? -1 : quantity);
        this.unit = (unit == null ? StringUtils.EMPTY : unit);
        this.tip = (tip == null ? StringUtils.EMPTY : tip);
    }
}
