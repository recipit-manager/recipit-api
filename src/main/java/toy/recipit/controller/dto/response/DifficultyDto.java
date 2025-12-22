package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "난이도 정보")
public class DifficultyDto {
    @Schema(description = "난이도 명", example = "쉬움")
    private final String difficultyName;
    @Schema(description = "난이도 코드", example = "D1")
    private final String difficultyCode;
}
