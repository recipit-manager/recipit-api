package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import toy.recipit.common.validation.Draft;
import toy.recipit.common.validation.Upload;

@Getter
public class IngredientDto {
    @NotBlank(message = "validation.recipe.ingredient.name.blank", groups = Upload.class)
    @Size(max = 10, message = "validation.recipe.ingredient.name.size", groups = {Draft.class, Upload.class})
    private final String name;

    @NotBlank(message = "validation.recipe.ingredient.category.blank", groups = Upload.class)
    private final String categoryCode;

    @NotNull(message = "validation.recipe.ingredient.quantity.null", groups = Upload.class)
    @Digits(integer = 5, fraction = 0, message = "validation.recipe.ingredient.quantity.size", groups = {Draft.class, Upload.class})
    private final Integer quantity;

    @NotBlank(message = "validation.recipe.ingredient.unit.blank", groups = Upload.class)
    @Size(max = 5, message = "validation.recipe.ingredient.unit.size", groups = {Draft.class, Upload.class})
    private final String unit;

    @Size(max = 30, message = "validation.recipe.ingredient.tip.size", groups = {Draft.class, Upload.class})
    private final String tip;

    public IngredientDto(String name, String categoryCode, Integer quantity, String unit, String tip) {
        this.name = (name == null ? StringUtils.EMPTY : name);
        this.categoryCode = (categoryCode == null ? StringUtils.EMPTY : categoryCode);
        this.quantity = (quantity == null ? -1 : quantity);
        this.unit = (unit == null ? StringUtils.EMPTY : unit);
        this.tip = (tip == null ? StringUtils.EMPTY : tip);
    }
}
