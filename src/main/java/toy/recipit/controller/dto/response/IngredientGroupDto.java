package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "재료 그룹 정보")
public class IngredientGroupDto {
    @Schema(description = "카테고리명", example = "채소")
    private final String categoryName;
    @Schema(description = "재료 목록")
    private final List<IngredientItemDto> ingredientList;
}