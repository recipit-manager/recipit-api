package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "조회할 페이지 정보")
public class GetPageDto {
    @NotNull(message = "recipe.page.null")
    @Min(value = 1, message = "recipe.list.page.min")
    @Schema(description = "페이지 번호", example = "1")
    private final Integer page;

    @NotNull(message = "recipe.size.null")
    @Min(value = 1, message = "recipe.list.size.min")
    @Schema(description = "페이지 당 레시피 수", example = "10")
    private final Integer size;
}
