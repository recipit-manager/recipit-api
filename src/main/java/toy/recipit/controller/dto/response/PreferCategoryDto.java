package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "선호 카테고리 정보")
public class PreferCategoryDto {
    @Schema(description = "레시피 카테고리 코드", example = "R01")
    private final String categoryCode;
    @Schema(description = "레시피 카테고리명", example = "메인요리")
    private final String categoryName;
    @Schema(description = "선호 상태코드", example = "RF01")
    private final String statusCode;
    @Schema(description = "선호 상태코드명", example = "선호")
    private final String statusName;
    @Schema(description = "카테고리 아이콘 url", example = "http://localhost:8080/images/category/R01.png")
    private final String iconUrl;
}
