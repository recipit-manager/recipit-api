package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UnMatchIngredientVo {
    private final String recipeNo;
    private final String ingredient;
}