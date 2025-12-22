package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "재료 카테고리 정보")
public class IngredientCategoryDto {
    @Schema(description = "재료 그룹 목록")
    private final List<IngredientGroupDto> ingredientGroupList;
    @Schema(description = "재료 카테고리 목록", example = "[IC01, IC02]")
    private final List<String> categoryList;
}