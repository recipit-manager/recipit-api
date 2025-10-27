package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InsertReportVo {
    private final String reportNo;
    private final String userNo;
    private final String recipeNo;
    private final String actionStatusCode;
}
