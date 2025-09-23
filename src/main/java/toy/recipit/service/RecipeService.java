package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.common.util.ImageKitUtil;
import toy.recipit.controller.dto.response.PopularRecipeDto;
import toy.recipit.mapper.RecipeMapper;
import toy.recipit.mapper.vo.PopularRecipeVo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final ImageKitUtil imageKitUtil;

    public List<PopularRecipeDto> getPopularRecipes(String userNo, int size) {
        List<PopularRecipeVo> popularRecipes =
                recipeMapper.getPopularRecipes(userNo, size, Constants.Image.THUMBNAIL);

        return popularRecipes.stream()
                .map(popularRecipeVo -> new PopularRecipeDto(
                        popularRecipeVo.getRecipeNo(),
                        popularRecipeVo.getTitle(),
                        imageKitUtil.getUrl(popularRecipeVo.getImagePath())
                                .orElseThrow(() -> new RuntimeException("잘못된 경로입니다 : " + popularRecipeVo.getImagePath())),
                        popularRecipeVo.getLikeCount(),
                        popularRecipeVo.getIsLiked()
                ))
                .toList();
    }

    public Integer getDraftRecipeCount(String userNo) {
        return recipeMapper.getRecipeCount(userNo, Constants.Recipe.DRAFT);
    }
}