package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
public class ReportRecipeDto {
    @NotEmpty(message = "validation.report.typeCodeList.notEmpty")
    private final List<String> reportTypeCodeList;

    @Size(max = 100, message = "validation.report.content.size")
    private final String content;

    public ReportRecipeDto(List<String> reportTypeCodeList, String content) {
        this.reportTypeCodeList = reportTypeCodeList;
        this.content = (content == null ? StringUtils.EMPTY : content);
    }
}
