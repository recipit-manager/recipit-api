package toy.recipit.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RecipeCategoryDto {
    private final String code;
    private final String codeName;
    private final String imageUrl;
}
