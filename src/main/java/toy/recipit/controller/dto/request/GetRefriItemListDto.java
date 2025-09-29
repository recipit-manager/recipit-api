package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetRefriItemListDto {
    @NotEmpty(message = "refriItem.keywordList.empty")
    private final List<String> keywordList;

    @NotNull(message = "recipe.page.null")
    @Min(value = 1, message = "recipe.list.page.min")
    private final Integer page;

    @NotNull(message = "recipe.size.null")
    @Min(value = 1, message = "recipe.list.size.min")
    private final Integer size;
}
