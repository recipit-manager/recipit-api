package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonDetailCodeVo {
    private final String code;
    private final String codeName;
    private final String note1;
    private final String note2;
    private final String note3;
    private final String note4;
}
