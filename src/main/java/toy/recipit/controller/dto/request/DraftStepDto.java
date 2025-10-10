package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

@Getter
public class DraftStepDto {
    @Size(max = 500, message = "validation.recipe.step.contents.size")
    private final String contents;

    @Size(max = 5, message = "validation.recipe.step.image.size")
    private final List<Integer> imageIndexes;

    public DraftStepDto(String contents, List<Integer> imageIndexes) {
        this.contents = (contents == null ? StringUtils.EMPTY : contents);
        this.imageIndexes = (imageIndexes == null ? Collections.emptyList() : imageIndexes);
    }
}
