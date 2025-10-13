package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecipeDetailVo {
    private String recipeNo;
    private String nickname;
    private String title;
    private String description;
    private Integer cookingTime;
    private Integer servingSize;
    private String difficulty;
    private Integer likeCount;
    private String statusCode;
    private String statusName;
    private String mainImagePath;
    private String likeYn;
    private String bookmarkYn;
    private String reportYn;
    private List<String> completionImagePathList;
}