package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class cmDetailCodeVo {
    private String groupCode;
    private String code;
    private String codeName;
    private String note1;
    private String note2;
    private String note3;
    private String note4;
    private Integer sortSequence;
    private String useYn;
    private LocalDateTime createDatetime;
    private Long createUser;
    private LocalDateTime editDatetime;
    private Long editUser;
}
