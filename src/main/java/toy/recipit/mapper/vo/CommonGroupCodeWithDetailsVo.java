package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommonGroupCodeWithDetailsVo {
    private String groupCode;
    private String groupCodeName;
    private List<CommonDetailCodeVo> commonCodeDetailVoList;
}
