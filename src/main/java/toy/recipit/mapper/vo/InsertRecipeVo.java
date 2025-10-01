package toy.recipit.mapper.vo;

import lombok.Getter;
import toy.recipit.controller.dto.request.DraftRecipeDto;

@Getter
public class InsertRecipeVo {
    private final String recipeNo;
    private final String userNo;
    private final String title;
    private final String description;
    private final String categoryCode;
    private final int cookingTime;
    private final int servingSize;
    private final String difficultyCode;
    private final String statusCode;

    public InsertRecipeVo(String userNo,
                          DraftRecipeDto draftRecipeDto,
                          String statusCode) {
        this.recipeNo = null;
        this.userNo = userNo;
        this.title = draftRecipeDto.getTitle();
        this.description = draftRecipeDto.getDescription();
        this.categoryCode = draftRecipeDto.getCategoryCode();
        this.cookingTime = draftRecipeDto.getCookingTime();
        this.servingSize = draftRecipeDto.getServingSize();
        this.difficultyCode = draftRecipeDto.getDifficultyCode();
        this.statusCode = statusCode;
    }
}
