package toy.recipit.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

@Getter
public class DraftRecipeDto {
    @Size(max = 20, message = "validation.recipe.title.size")
    private final String title;

    @Size(max = 200, message = "validation.recipe.description.size")
    private final String description;

    private final String categoryCode;

    @Digits(integer = 3, fraction = 0, message = "validation.recipe.cookingTime.size")
    private final Integer cookingTime;

    @Digits(integer = 2, fraction = 0, message = "validation.recipe.servingSize.size")
    private final Integer servingSize;

    private final String difficultyCode;

    @Valid
    @Size(max = 50, message = "validation.recipe.ingredient.size")
    private final List<IngredientDto> ingredientList;

    @Valid
    @Size(max = 20, message = "validation.recipe.step.size")
    private final List<StepDto> stepList;

    public DraftRecipeDto(
            String title,
            String description,
            String categoryCode,
            Integer cookingTime,
            Integer servingSize,
            String difficultyCode,
            List<IngredientDto> ingredientList,
            List<StepDto> stepList
    ) {
        this.title = (title == null ? StringUtils.EMPTY : title);
        this.description = (description == null ? StringUtils.EMPTY : description);
        this.categoryCode = (categoryCode == null ? StringUtils.EMPTY : categoryCode);
        this.cookingTime = (cookingTime == null ? -1 : cookingTime);
        this.servingSize = (servingSize == null ? -1 : servingSize);
        this.difficultyCode = (difficultyCode == null ? StringUtils.EMPTY : difficultyCode);
        this.ingredientList = (ingredientList == null ? Collections.emptyList() : ingredientList);
        this.stepList = (stepList == null ? Collections.emptyList() : stepList);
    }
}
