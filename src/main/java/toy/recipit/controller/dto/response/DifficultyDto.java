package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DifficultyDto {
    private final String difficultyName;
    private final String difficultyCode;
}
