package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PopularRecipeVo {
    private final String recipeNo;
    private final String title;
    private final String imagePath;
    private final int likeCount;
    private final Boolean isLiked;
}
