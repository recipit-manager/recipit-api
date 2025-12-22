package toy.recipit.controller.dto.request;

import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "레시피 조회 조건 정보")
public class GetRecipeListDto {
    @Schema(description = "조회 할 카테고리 코드", example = "R01")
    private final String categoryCode;
    @Schema(description = "검색 키워드", example = "김치")
    private final String keyword;

    @NotNull(message = "recipe.page.null")
    @Min(value = 1, message = "recipe.list.page.min")
    @Schema(description = "검색 페이지", example = "1")
    private final Integer page;

    @NotNull(message = "recipe.size.null")
    @Min(value = 1, message = "recipe.list.size.min")
    @Schema(description = "한 페이지 당 레시피 수", example = "10")
    private final Integer size;

    public GetRecipeListDto(
            String categoryCode,
            String keyword,
            Integer page,
            Integer size
    ) {
        this.categoryCode = (categoryCode == null) ? StringUtil.EMPTY_STRING : categoryCode;
        this.keyword = (keyword == null) ? StringUtil.EMPTY_STRING : keyword;
        this.page = page;
        this.size = size;
    }
}
