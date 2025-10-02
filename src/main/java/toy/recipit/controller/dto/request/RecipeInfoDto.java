package toy.recipit.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import toy.recipit.common.validation.Draft;
import toy.recipit.common.validation.Upload;

import java.util.Collections;
import java.util.List;

@Getter
public class RecipeInfoDto {
    @NotBlank(message = "validation.recipe.title.blank", groups = Upload.class)
    @Size(max = 20, message = "validation.recipe.title.size", groups = {Draft.class, Upload.class})
    private final String title;

    @NotBlank(message = "validation.recipe.description.blank", groups = Upload.class)
    @Size(max = 200, message = "validation.recipe.description.size", groups = {Draft.class, Upload.class})
    private final String description;

    @NotBlank(message = "validation.recipe.category.blank", groups = Upload.class)
    private final String categoryCode;

    @NotNull(message = "validation.recipe.cookingTime.null", groups = Upload.class)
    @Digits(integer = 3, fraction = 0, message = "validation.recipe.cookingTime.size", groups = {Draft.class, Upload.class})
    private final Integer cookingTime;

    @NotNull(message = "validation.recipe.servingSize.null", groups = Upload.class)
    @Digits(integer = 2, fraction = 0, message = "validation.recipe.servingSize.size", groups = {Draft.class, Upload.class})
    private final Integer servingSize;

    @NotBlank(message = "validation.recipe.difficulty.blank", groups = Upload.class)
    private final String difficultyCode;

    @Valid
    @NotEmpty(message = "validation.recipe.ingredient.empty", groups = Upload.class)
    @Size(max = 50, message = "validation.recipe.ingredient.size", groups = {Draft.class, Upload.class})
    private final List<IngredientDto> ingredientList;

    @Valid
    @NotEmpty(message = "validation.recipe.step.empty", groups = Upload.class)
    @Size(max = 20, message = "validation.recipe.step.size", groups = {Draft.class, Upload.class})
    private final List<StepDto> stepList;

    public RecipeInfoDto(
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
