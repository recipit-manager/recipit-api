package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import toy.recipit.common.validation.Draft;
import toy.recipit.common.validation.Upload;

import java.util.Collections;
import java.util.List;

@Getter
public class StepDto {
    @NotBlank(message = "validation.recipe.step.contents.blank", groups = Upload.class)
    @Size(max = 500, message = "validation.recipe.step.contents.size", groups = {Draft.class, Upload.class})
    private final String contents;

    @Size(max = 5, message = "validation.recipe.step.image.size", groups = {Draft.class, Upload.class})
    private final List<Integer> imageIndexes;

    public StepDto(String contents, List<Integer> imageIndexes) {
        this.contents = (contents == null ? StringUtils.EMPTY : contents);
        this.imageIndexes = (imageIndexes == null ? Collections.emptyList() : imageIndexes);
    }
}
