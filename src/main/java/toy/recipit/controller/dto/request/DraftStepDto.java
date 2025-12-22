package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

@Getter
@Schema(description = "임시저장 레시피 조리 단계 정보")
public class DraftStepDto {
    @Size(max = 500, message = "validation.recipe.step.contents.size")
    @Schema(description = "조리 단계 내용", example = "돼지고기를 삶아줍니다.")
    private final String contents;

    @Size(max = 5, message = "validation.recipe.step.image.size")
    @Schema(description = "단계 이미지 배열")
    private final List<Integer> imageIndexes;

    public DraftStepDto(String contents, List<Integer> imageIndexes) {
        this.contents = (contents == null ? StringUtils.EMPTY : contents);
        this.imageIndexes = (imageIndexes == null ? Collections.emptyList() : imageIndexes);
    }
}
