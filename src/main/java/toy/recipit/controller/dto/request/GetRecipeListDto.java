package toy.recipit.controller.dto.request;

import io.netty.util.internal.StringUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GetRecipeListDto {
    private final String categoryCode;
    private final String keyword;

    @NotNull(message = "recipe.page.null")
    @Min(value = 1, message = "recipe.list.page.min")
    private final Integer page;

    @NotNull(message = "recipe.size.null")
    @Min(value = 1, message = "recipe.list.size.min")
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
