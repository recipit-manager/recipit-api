package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import toy.recipit.common.validation.Draft;
import toy.recipit.common.validation.Upload;

@Getter
@RequiredArgsConstructor
public class UploadIngredientDto {
    @NotBlank(message = "validation.recipe.ingredient.name.blank")
    @Size(max = 10, message = "validation.recipe.ingredient.name.size")
    private final String name;

    @NotBlank(message = "validation.recipe.ingredient.category.blank")
    private final String categoryCode;

    @NotNull(message = "validation.recipe.ingredient.quantity.null")
    @Digits(integer = 5, fraction = 0, message = "validation.recipe.ingredient.quantity.size")
    private final Integer quantity;

    @NotBlank(message = "validation.recipe.ingredient.unit.blank")
    @Size(max = 5, message = "validation.recipe.ingredient.unit.size")
    private final String unit;

    @Size(max = 30, message = "validation.recipe.ingredient.tip.size")
    private final String tip;
}
