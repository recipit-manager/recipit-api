package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IngredientVo {
    private final String name;
    private final String categoryCode;
    private final String quantity;
    private final String unit;
    private final String tip;
}
