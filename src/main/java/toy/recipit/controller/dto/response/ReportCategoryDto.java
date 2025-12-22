package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "신고 카테고리 정보")
public class ReportCategoryDto {
    @Schema(description = "카테고리 코드", example = "NT01")
    private final String categoryCode;
    @Schema(description = "카테고리명", example = "저작권")
    private final String categoryName;
}
