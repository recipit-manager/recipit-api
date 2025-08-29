package toy.recipit.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IngredientItemDto {
    private final String name;
    private final String iconUrl;
}
