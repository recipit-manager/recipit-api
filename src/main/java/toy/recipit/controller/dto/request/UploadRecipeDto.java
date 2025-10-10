package toy.recipit.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UploadRecipeDto {
    @NotBlank(message = "validation.recipe.title.blank")
    @Size(max = 20, message = "validation.recipe.title.size")
    private final String title;

    @NotBlank(message = "validation.recipe.description.blank")
    @Size(max = 200, message = "validation.recipe.description.size")
    private final String description;

    @NotBlank(message = "validation.recipe.category.blank")
    private final String categoryCode;

    @NotNull(message = "validation.recipe.cookingTime.null")
    @Digits(integer = 3, fraction = 0, message = "validation.recipe.cookingTime.size")
    private final Integer cookingTime;

    @NotNull(message = "validation.recipe.servingSize.null")
    @Digits(integer = 2, fraction = 0, message = "validation.recipe.servingSize.size")
    private final Integer servingSize;

    @NotBlank(message = "validation.recipe.difficulty.blank")
    private final String difficultyCode;

    @Valid
    @NotEmpty(message = "validation.recipe.ingredient.empty")
    @Size(max = 50, message = "validation.recipe.ingredient.size")
    private final List<UploadIngredientDto> ingredientList;

    @Valid
    @NotEmpty(message = "validation.recipe.step.empty")
    @Size(max = 20, message = "validation.recipe.step.size")
    private final List<UploadStepDto> stepList;
}
