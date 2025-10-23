package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InsertReportDetailVo {
    private final String reportNo;
    private final String categoryCode;
    private final String detail;
    private final String userNo;
}