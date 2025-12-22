package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "레시피 목록 정보")
public class RecipeListDto {
    @Schema(description = "레시피 목록")
    private final List<RecipeDto> recipelist;
    @Schema(description = "존재하는 레시피 카테고리 목록")
    private final List<CommonCodeAndNameDto> categorylist;
}
