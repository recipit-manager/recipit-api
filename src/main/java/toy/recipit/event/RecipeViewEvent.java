package toy.recipit.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecipeViewEvent {
    private final String userNo;
    private final String recipeNo;
}
