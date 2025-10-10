package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class DraftIngredientDto {
    @Size(max = 10, message = "validation.recipe.ingredient.name.size")
    private final String name;

    private final String categoryCode;

    @Digits(integer = 5, fraction = 0, message = "validation.recipe.ingredient.quantity.size")
    private final Integer quantity;

    @Size(max = 5, message = "validation.recipe.ingredient.unit.size")
    private final String unit;

    @Size(max = 30, message = "validation.recipe.ingredient.tip.size")
    private final String tip;

    public DraftIngredientDto(String name, String categoryCode, Integer quantity, String unit, String tip) {
        this.name = (name == null ? StringUtils.EMPTY : name);
        this.categoryCode = (categoryCode == null ? StringUtils.EMPTY : categoryCode);
        this.quantity = (quantity == null ? -1 : quantity);
        this.unit = (unit == null ? StringUtils.EMPTY : unit);
        this.tip = (tip == null ? StringUtils.EMPTY : tip);
    }
}
