package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "공통코드 정보")
public class CommonCodeAndNameDto {
    @Schema(description = "코드", example = "D1")
    private final String code;
    @Schema(description = "코드명", example = "쉬움")
    private final String codeName;
}
