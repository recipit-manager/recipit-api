package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "선호 상태 변경 정보")
public class EditPreferCategoryDto {
    @NotBlank(message = "validation.recipe.category.blank")
    @Schema(description = "변경 할 레시피 카테고리", example = "R01")
    private final String categoryCode;
    @NotBlank(message = "validation.recipe.category.status.blank")
    @Schema(description = "변경 할 상태", example = "RF01")
    private final String statusCode;
}
