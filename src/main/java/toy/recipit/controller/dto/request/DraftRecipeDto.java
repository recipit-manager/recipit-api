package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import toy.recipit.common.Constants;

import java.util.Collections;
import java.util.List;

@Getter
@Schema(description = "임시저장 할 레시피 정보")
public class DraftRecipeDto {
    @Size(max = 20, message = "validation.recipe.title.size")
    @Schema(description = "레시피 제목", example = "김치찌개")
    private final String title;

    @Size(max = 200, message = "validation.recipe.description.size")
    @Schema(description = "레시피 소개", example = "김치찌개 입니다.")
    private final String description;

    @Schema(description = "레시피 카테고리 코드", example = "R01")
    private final String categoryCode;

    @Digits(integer = 3, fraction = 0, message = "validation.recipe.cookingTime.size")
    @Schema(description = "조리 시간", example = "15")
    private final Integer cookingTime;

    @Digits(integer = 2, fraction = 0, message = "validation.recipe.servingSize.size")
    @Schema(description = "조리 기준량", example = "3")
    private final Integer servingSize;

    @Schema(description = "난이도 코드", example = "D1")
    private final String difficultyCode;

    @Valid
    @Size(max = 50, message = "validation.recipe.ingredient.size")
    @Schema(description = "임시저장 레시피 재료 정보 목록")
    private final List<DraftIngredientDto> ingredientList;

    @Valid
    @Size(max = 20, message = "validation.recipe.step.size")
    @Schema(description = "임시저장 레시피 조리 단계 정보 목록")
    private final List<DraftStepDto> stepList;

    public DraftRecipeDto(
            String title,
            String description,
            String categoryCode,
            Integer cookingTime,
            Integer servingSize,
            String difficultyCode,
            List<DraftIngredientDto> ingredientList,
            List<DraftStepDto> stepList
    ) {
        this.title = (title == null ? StringUtils.EMPTY : title);
        this.description = (description == null ? StringUtils.EMPTY : description);
        this.categoryCode = (categoryCode == null ? StringUtils.EMPTY : categoryCode);
        this.cookingTime = (cookingTime == null ? -1 : cookingTime);
        this.servingSize = (servingSize == null ? -1 : servingSize);
        this.difficultyCode = (difficultyCode == null ? Constants.Difficulty.NORMAL : difficultyCode);
        this.ingredientList = (ingredientList == null ? Collections.emptyList() : ingredientList);
        this.stepList = (stepList == null ? Collections.emptyList() : stepList);
    }
}
