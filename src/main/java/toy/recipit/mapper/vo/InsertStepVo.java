package toy.recipit.mapper.vo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InsertStepVo {
    private final String stepNo;
    private final String recipeNo;
    private final String contents;
    private final int sortSequence;
}