package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class UploadStepDto {
    @NotBlank(message = "validation.recipe.step.contents.blank")
    @Size(max = 500, message = "validation.recipe.step.contents.size")
    private final String contents;

    @Size(max = 5, message = "validation.recipe.step.image.size")
    private final List<Integer> imageIndexes;
}
