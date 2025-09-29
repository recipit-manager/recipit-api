package toy.recipit.controller.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefriItemRecipeListDto {

    private final String id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final Integer cookingTime;
    private final Integer servingSize;
    private final String difficulty;
    private final String difficultyCode;
    private final Integer likeCount;
    private final Boolean isLiked;
    private final List<String> unMatchIngredientlist;
}