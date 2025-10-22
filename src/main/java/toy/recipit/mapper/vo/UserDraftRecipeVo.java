package toy.recipit.mapper.vo;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class UserDraftRecipeVo {
    private final String recipeNo;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final int cookingTime;
    private final String difficulty;

    public UserDraftRecipeVo(
            String recipeNo,
            String title,
            String description,
            String ImageUrl,
            Integer cookingTime,
            String difficulty
    ) {
        this.recipeNo = recipeNo;
        this.name = title;
        this.description = description;
        this.imageUrl = (ImageUrl == null ? StringUtils.EMPTY : ImageUrl);
        this.cookingTime = cookingTime;
        this.difficulty = difficulty;
    }
}
