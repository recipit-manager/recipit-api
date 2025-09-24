package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RecipeListDto {
    private final List<RecipeDto> recipelist;
    private final List<CommonCodeAndNameDto> categorylist;
}
