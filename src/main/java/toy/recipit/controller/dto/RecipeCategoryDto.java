package toy.recipit.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecipeCategoryDto {
    private final String categoryCode;
    private final String categoryName;
    private final String iconUrl;
}
