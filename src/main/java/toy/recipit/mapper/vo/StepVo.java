package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StepVo {
    private String content;
    private List<String> imagePathList;
}