package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "국가 코드 정보")
public class CountryCodeDto {
    @Schema(description = "국가 코드", example = "KR")
    private final String code;
    @Schema(description = "국가명", example = "대한민국")
    private final String name;
    @Schema(description = "국가 전화번호 코드", example = "+82")
    private final String dialCode;
    @Schema(description = "전화번호 형식", example = "000-0000-0000")
    private final String format;
    @Schema(description = "전화번호 검증을 위한 정규표현식", example = "^01[016789]-?[0-9]{3,4}-?[0-9]{4}$")
    private final String regex;
}
