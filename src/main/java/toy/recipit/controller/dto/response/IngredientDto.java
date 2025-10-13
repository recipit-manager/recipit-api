package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IngredientDto {
    private final String name;
    private final String categoryCode;
    private final String quantity;
    private final String unit;
    private final String tip;
}