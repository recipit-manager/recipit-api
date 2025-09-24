package toy.recipit.controller.dto.request;

import io.netty.util.internal.StringUtil;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class GetRecipeListDto {
    private final String categoryCode;
    private final String keyword;
    @Min(value = 1, message = "recipe.list.page.min")
    private final int page;
    @Min(value = 1, message = "recipe.list.size.min")
    private final int size;

    public GetRecipeListDto(
            String categoryCode,
            String keyword,
            int page,
            int size
    ) {
        this.categoryCode = (categoryCode == null) ? StringUtil.EMPTY_STRING : categoryCode;
        this.keyword = (keyword == null) ? StringUtil.EMPTY_STRING : keyword;
        this.page = page;
        this.size = size;
    }
}
