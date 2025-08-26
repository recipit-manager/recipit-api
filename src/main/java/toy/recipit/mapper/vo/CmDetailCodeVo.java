package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CmDetailCodeVo {
    private final String groupCode;
    private final String code;
    private final String codeName;
    private final String note1;
    private final String note2;
    private final String note3;
    private final String note4;
    private final Integer sortSequence;
    private final String useYn;
    private final LocalDateTime createDatetime;
    private final Long createUser;
    private final LocalDateTime editDatetime;
    private final Long editUser;
}
