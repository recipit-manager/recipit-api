package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UploadIngredientDto {
    @NotBlank(message = "validation.recipe.ingredient.name.blank")
    @Size(max = 10, message = "validation.recipe.ingredient.name.size")
    @Schema(description = "재료명", example = "돼지고기")
    private final String name;

    @NotBlank(message = "validation.recipe.ingredient.category.blank")
    @Schema(description = "재료 카테고리 코드", example = "I01")
    private final String categoryCode;

    @NotNull(message = "validation.recipe.ingredient.quantity.null")
    @Digits(integer = 5, fraction = 0, message = "validation.recipe.ingredient.quantity.size")
    @Schema(description = "수량", example = "1")
    private final Integer quantity;

    @NotBlank(message = "validation.recipe.ingredient.unit.blank")
    @Size(max = 5, message = "validation.recipe.ingredient.unit.size")
    @Schema(description = "단위", example = "g")
    private final String unit;

    @Size(max = 30, message = "validation.recipe.ingredient.tip.size")
    @Schema(description = "재료tip", example = "돼지고기는 실온에서 냉기를 빼주세요.")
    private final String tip;
}
