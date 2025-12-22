package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Schema(description = "레시피 신고 정보")
public class ReportRecipeDto {
    @NotEmpty(message = "validation.report.typeCodeList.notEmpty")
    @Schema(description = "레시피 신고 카테고리 코드 목록", example = "[RP01, RP02]")
    private final List<String> reportTypeCodeList;

    @Size(max = 100, message = "validation.report.content.size")
    @Schema(description = "레시피 신고 내용", example = "허위정보 레시피입니다.")
    private final String content;

    public ReportRecipeDto(List<String> reportTypeCodeList, String content) {
        this.reportTypeCodeList = reportTypeCodeList;
        this.content = (content == null ? StringUtils.EMPTY : content);
    }
}
