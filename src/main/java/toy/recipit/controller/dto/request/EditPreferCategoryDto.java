package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EditPreferCategoryDto {
    @NotBlank(message = "validation.recipe.category.blank")
    private final String categoryCode;
    @NotBlank(message = "validation.recipe.category.status.blank")
    private final String statusCode;
}
