package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "레시피 제목", example = "김치찌개")
    private final String title;

    @NotBlank(message = "validation.recipe.description.blank")
    @Size(max = 200, message = "validation.recipe.description.size")
    @Schema(description = "레시피 소개", example = "김치찌개 입니다.")
    private final String description;

    @NotBlank(message = "validation.recipe.category.blank")
    @Schema(description = "레시피 카테고리 코드", example = "R01")
    private final String categoryCode;

    @NotNull(message = "validation.recipe.cookingTime.null")
    @Digits(integer = 3, fraction = 0, message = "validation.recipe.cookingTime.size")
    @Schema(description = "조리 시간", example = "15")
    private final Integer cookingTime;

    @NotNull(message = "validation.recipe.servingSize.null")
    @Digits(integer = 2, fraction = 0, message = "validation.recipe.servingSize.size")
    @Schema(description = "조리 기준량", example = "3")
    private final Integer servingSize;

    @NotBlank(message = "validation.recipe.difficulty.blank")
    @Schema(description = "난이도 코드", example = "D1")
    private final String difficultyCode;

    @Valid
    @NotEmpty(message = "validation.recipe.ingredient.empty")
    @Size(max = 50, message = "validation.recipe.ingredient.size")
    @Schema(description = "레시피 재료 정보 목록")
    private final List<UploadIngredientDto> ingredientList;

    @Valid
    @NotEmpty(message = "validation.recipe.step.empty")
    @Size(max = 20, message = "validation.recipe.step.size")
    @Schema(description = "레시피 조리 단계 정보 목록")
    private final List<UploadStepDto> stepList;
}
