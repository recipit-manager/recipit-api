package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Schema(description = "냉템요리 검색 조건 정보")
public class GetRefriItemListDto {
    @NotEmpty(message = "refriItem.keywordList.empty")
    @Schema(description = "검색 키워드 목록", example = "[감자, 고구마]")
    private final List<String> keywordList;

    @NotNull(message = "recipe.page.null")
    @Min(value = 1, message = "recipe.list.page.min")
    @Schema(description = "검색 페이지", example = "1")
    private final Integer page;

    @NotNull(message = "recipe.size.null")
    @Min(value = 1, message = "recipe.list.size.min")
    @Schema(description = "페이지 당 레시피 수", example = "10")
    private final Integer size;
}
