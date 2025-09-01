package toy.recipit.controller.responseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class IngredientGroupDto {
    private final String categoryName;
    private final List<IngredientItemDto> ingredientList;
}