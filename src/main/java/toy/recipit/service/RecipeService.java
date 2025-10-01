package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toy.recipit.common.Constants;
import toy.recipit.common.util.ImageKitUtil;
import toy.recipit.controller.dto.request.DraftRecipeDto;
import toy.recipit.controller.dto.request.GetRecipeListDto;
import toy.recipit.controller.dto.request.IngredientDto;
import toy.recipit.controller.dto.request.StepDto;
import toy.recipit.controller.dto.response.CommonCodeAndNameDto;
import toy.recipit.controller.dto.response.PopularRecipeDto;
import toy.recipit.controller.dto.response.RecipeDto;
import toy.recipit.controller.dto.response.RecipeListDto;
import toy.recipit.mapper.RecipeMapper;
import toy.recipit.mapper.vo.CommonDetailCodeVo;
import toy.recipit.mapper.vo.InsertRecipeVo;
import toy.recipit.mapper.vo.PopularRecipeVo;
import toy.recipit.mapper.vo.SearchRecipeVo;
import toy.recipit.mapper.vo.StepVo;

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

    public int getDraftRecipeCount(String userNo) {
        return recipeMapper.getRecipeCount(userNo, Constants.Recipe.DRAFT);
    }

    @Transactional
    public Boolean likeRecipe(String userNo, String recipeNo) {
        if(!recipeMapper.isRecipeExists(recipeNo)) {
            throw new IllegalArgumentException("recipe.notFoundRecipe");
        }

        if (recipeMapper.isLikeExists(userNo, recipeNo)) {
            recipeMapper.updateLike(userNo, recipeNo, Constants.Yn.YES);
        } else {
            recipeMapper.insertLike(userNo, recipeNo, Constants.Yn.YES);
        }

        return true;
    }

    @Transactional
    public Boolean unlikeRecipe(String userNo, String recipeNo) {
        if(!recipeMapper.isRecipeExists(recipeNo)) {
            throw new IllegalArgumentException("recipe.notFoundRecipe");
        }

        recipeMapper.updateLike(userNo, recipeNo, Constants.Yn.NO);

        return true;
    }

    public RecipeListDto getRecentRecipes(String userNo, GetRecipeListDto getRecipeListDto) {
        List<SearchRecipeVo> recipeVoList = recipeMapper.getRecipes(
                userNo,
                getRecipeListDto.getCategoryCode(),
                getRecipeListDto.getKeyword(),
                (getRecipeListDto.getPage() - 1) * getRecipeListDto.getSize(),
                getRecipeListDto.getSize(),
                Constants.Image.THUMBNAIL,
                Constants.GroupCode.DIFFICULTY,
                Constants.SortType.RECENT
        );

        return createRecipeListDto(recipeVoList, getRecipeListDto);
    }

    public RecipeListDto getLikeRecipes(String userNo, GetRecipeListDto getRecipeListDto) {
        List<SearchRecipeVo> recipeVoList = recipeMapper.getRecipes(
                userNo,
                getRecipeListDto.getCategoryCode(),
                getRecipeListDto.getKeyword(),
                (getRecipeListDto.getPage() - 1) * getRecipeListDto.getSize(),
                getRecipeListDto.getSize(),
                Constants.Image.THUMBNAIL,
                Constants.GroupCode.DIFFICULTY,
                Constants.SortType.LIKE
        );

        return createRecipeListDto(recipeVoList, getRecipeListDto);
    }

    private RecipeListDto createRecipeListDto(List<SearchRecipeVo> recipeVoList, GetRecipeListDto getRecipeListDto) {
        List<RecipeDto> recipelist = recipeVoList.stream()
                .map(searchRecipeVo -> new RecipeDto(
                        searchRecipeVo.getRecipeNo(),
                        searchRecipeVo.getCategoryCode(),
                        searchRecipeVo.getName(),
                        searchRecipeVo.getDescription(),
                        imageKitUtil.getUrl(searchRecipeVo.getImageUrl())
                                .orElseThrow(() -> new RuntimeException("잘못된 경로입니다 : " + searchRecipeVo.getImageUrl())),
                        searchRecipeVo.getCookingTime(),
                        searchRecipeVo.getDifficultyCode(),
                        searchRecipeVo.getDifficultyCodeName(),
                        searchRecipeVo.getLikeCount(),
                        searchRecipeVo.getIsLiked()
                ))
                .toList();

        List<CommonDetailCodeVo> categoryVoList = recipeMapper.getRecipeCategories(
                getRecipeListDto.getKeyword(),
                Constants.GroupCode.RECIPE_CATEGORY
        );

        List<CommonCodeAndNameDto> categorylist = categoryVoList.stream()
                .map(commonDetailCodeVo
                        -> new CommonCodeAndNameDto(commonDetailCodeVo.getCode(), commonDetailCodeVo.getCodeName()))
                .toList();

        return new RecipeListDto(recipelist, categorylist);
    }

    @Transactional
    public Boolean saveDraftRecipe(String userNo,
                                   DraftRecipeDto recipeInfo,
                                   MultipartFile mainImage,
                                   MultipartFile[] stepImages,
                                   MultipartFile[] completionImages) {

        InsertRecipeVo recipe = new InsertRecipeVo(
                userNo,
                recipeInfo,
                Constants.Recipe.DRAFT
        );

        recipeMapper.insertRecipe(recipe);
        String recipeNo = recipe.getRecipeNo();

        insertIngredients(recipeNo, userNo, recipeInfo.getIngredientList());

        insertImages(recipeNo, userNo, mainImage, completionImages);

        insertSteps(recipeNo, userNo, recipeInfo, stepImages);

        return true;
    }

    private void insertIngredients(String recipeNo, String userNo, List<IngredientDto> ingredientList) {
        if (!ingredientList.isEmpty()) {
            recipeMapper.insertIngredients(recipeNo, userNo, ingredientList);
        }
    }

    private void insertImages(String recipeNo,
                              String userNo,
                              MultipartFile mainImage,
                              MultipartFile[] completionImages) {
        try {
            int sortSequence = 0;

            if (mainImage != null && !mainImage.isEmpty()) {
                recipeMapper.insertRecipeImage(recipeNo, imageKitUtil.upload(mainImage), Constants.Image.THUMBNAIL, sortSequence, userNo);
            }

            if (completionImages != null) {
                for (MultipartFile completionImage : completionImages) {
                    recipeMapper.insertRecipeImage(recipeNo, imageKitUtil.upload(completionImage), Constants.Image.COMPLETE, sortSequence++, userNo);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void insertSteps(String recipeNo,
                             String userNo,
                             DraftRecipeDto recipeInfo,
                             MultipartFile[] stepImages) {
        if (recipeInfo.getStepList().isEmpty()) {
            return;
        }

        int stepSequence = 0;

        for (StepDto stepDto : recipeInfo.getStepList()) {
            StepVo stepVo = new StepVo(
                    null,
                    recipeNo,
                    stepDto.getContents(),
                    stepSequence++
            );

            recipeMapper.insertStep(stepVo, userNo);

            if (stepDto.getImageIndexes() == null || stepImages == null) {
                continue;
            }

            try {
                int imgSequence = 0;

                for (int idx : stepDto.getImageIndexes()) {
                    MultipartFile stepImage = stepImages[idx];
                    recipeMapper.insertStepImage(
                            stepVo.getStepNo(),
                            imageKitUtil.upload(stepImage),
                            imgSequence++,
                            userNo
                    );
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}