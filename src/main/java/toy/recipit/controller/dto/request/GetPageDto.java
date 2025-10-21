package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetPageDto {
    @NotNull(message = "recipe.page.null")
    @Min(value = 1, message = "recipe.list.page.min")
    private final Integer page;

    @NotNull(message = "recipe.size.null")
    @Min(value = 1, message = "recipe.list.size.min")
    private final Integer size;
}
