package toy.recipit.controller.responseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IngredientTypeDto {
    private final String categoryCode;
    private final String categoryName;
}
