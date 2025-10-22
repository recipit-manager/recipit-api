package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toy.recipit.common.Constants;
import toy.recipit.common.util.ImageKitUtil;
import toy.recipit.controller.dto.request.DraftIngredientDto;
import toy.recipit.controller.dto.request.DraftRecipeDto;
import toy.recipit.controller.dto.request.DraftStepDto;
import toy.recipit.controller.dto.request.EditPreferCategoryDto;
import toy.recipit.controller.dto.request.GetPageDto;
import toy.recipit.controller.dto.request.GetRecipeListDto;
import toy.recipit.controller.dto.request.UploadIngredientDto;
import toy.recipit.controller.dto.request.UploadRecipeDto;
import toy.recipit.controller.dto.request.UploadStepDto;
import toy.recipit.controller.dto.response.CommonCodeAndNameDto;
import toy.recipit.controller.dto.response.IngredientDto;
import toy.recipit.controller.dto.response.PopularRecipeDto;
import toy.recipit.controller.dto.response.PreferCategoryDto;
import toy.recipit.controller.dto.response.RecipeDetailDto;
import toy.recipit.controller.dto.response.RecipeDto;
import toy.recipit.controller.dto.response.RecipeListDto;
import toy.recipit.controller.dto.response.StepDto;
import toy.recipit.controller.dto.response.UserDraftRecipeDto;
import toy.recipit.controller.dto.response.UserRecipeDto;
import toy.recipit.event.RecipeViewEvent;
import toy.recipit.mapper.RecipeMapper;
import toy.recipit.mapper.vo.CommonDetailCodeVo;
import toy.recipit.mapper.vo.IngredientVo;
import toy.recipit.mapper.vo.InsertRecipeVo;
import toy.recipit.mapper.vo.InsertStepVo;
import toy.recipit.mapper.vo.PopularRecipeVo;
import toy.recipit.mapper.vo.PreferCategoryVo;
import toy.recipit.mapper.vo.RecipeDetailVo;
import toy.recipit.mapper.vo.SearchRecipeVo;
import toy.recipit.mapper.vo.StepVo;
import toy.recipit.mapper.vo.UserDraftRecipeVo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final ImageKitUtil imageKitUtil;
    private final ApplicationEventPublisher applicationEventPublisher;

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

    public int getRecipeCount(String userNo) {
        return recipeMapper.getRecipeCount(userNo, Constants.Recipe.RELEASE);
    }

    public int getUserLikeCount(String userNo) {
        return recipeMapper.getUserLikeCount(userNo, Constants.Recipe.RELEASE);
    }

    public int getUserBookmarkCount(String userNo) {
        return recipeMapper.getUserBookmarkCount(userNo);
    }

    @Transactional
    public Boolean likeRecipe(String userNo, String recipeNo) {
        if (!recipeMapper.isRecipeExists(recipeNo)) {
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
        if (!recipeMapper.isRecipeExists(recipeNo)) {
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
                                   MultipartFile[] completionImages) throws Exception {

        InsertRecipeVo recipe = new InsertRecipeVo(
                userNo,
                recipeInfo,
                Constants.Recipe.DRAFT
        );

        recipeMapper.insertRecipe(recipe);
        String recipeNo = recipe.getRecipeNo();

        insertDraftIngredients(recipeNo, userNo, recipeInfo.getIngredientList());

        insertImages(recipeNo, userNo, mainImage, completionImages);

        insertDraftSteps(recipeNo, userNo, recipeInfo, stepImages);

        return true;
    }

    @Transactional
    public Boolean uploadRecipe(String userNo,
                                UploadRecipeDto recipeInfo,
                                MultipartFile mainImage,
                                MultipartFile[] stepImages,
                                MultipartFile[] completionImages) throws Exception {

        InsertRecipeVo recipe = new InsertRecipeVo(
                userNo,
                recipeInfo,
                Constants.Recipe.RELEASE
        );

        recipeMapper.insertRecipe(recipe);
        String recipeNo = recipe.getRecipeNo();

        insertUploadIngredients(recipeNo, userNo, recipeInfo.getIngredientList());

        insertImages(recipeNo, userNo, mainImage, completionImages);

        insertUploadSteps(recipeNo, userNo, recipeInfo, stepImages);

        return true;
    }

    public RecipeDetailDto getRecipeDetail(String recipeNo, String userNo) {
        RecipeDetailVo recipeDetailVo = getRecipeDetailVo(recipeNo, userNo);

        List<IngredientDto> ingredientDtoList = getIngredientDtoList(recipeNo);

        List<StepDto> stepDtoList = getStepDtoList(recipeNo);

        String mainImageUrl = imageKitUtil.getUrl(recipeDetailVo.getMainImagePath())
                .orElseThrow(() -> new RuntimeException("잘못된 경로입니다: " + recipeDetailVo.getMainImagePath()));

        List<String> completionImageUrls = getCompletionImageUrls(recipeDetailVo);

        if(StringUtils.isNotEmpty(userNo) && Constants.Recipe.RELEASE.equals(recipeDetailVo.getStatusCode())) {
            applicationEventPublisher.publishEvent(new RecipeViewEvent(userNo, recipeNo));
        }

        return buildRecipeDetailDto(recipeDetailVo, mainImageUrl, completionImageUrls, ingredientDtoList, stepDtoList);
    }

    @Transactional
    public Boolean bookmarkRecipe(String userNo, String recipeNo) {
        if (!recipeMapper.isRecipeExists(recipeNo)) {
            throw new IllegalArgumentException("recipe.notFoundRecipe");
        }

        recipeMapper.insertBookmark(userNo, recipeNo);

        return true;
    }

    @Transactional
    public Boolean unBookmarkRecipe(String userNo, String recipeNo) {
        if (!recipeMapper.isRecipeExists(recipeNo)) {
            throw new IllegalArgumentException("recipe.notFoundRecipe");
        }

        recipeMapper.deleteBookmark(userNo, recipeNo);

        return true;
    }

    @Transactional
    public List<PreferCategoryDto> getPreferenceCategories(String userNo) {
        if (!recipeMapper.isPreferCategoriesExists(userNo)) {
            recipeMapper.insertPreferCategories(
                    userNo,
                    Constants.GroupCode.RECIPE_CATEGORY,
                    Constants.PreferCategory.AVERAGE
            );
        }

        List<PreferCategoryVo> preferCategoryVoList = recipeMapper.getPreferenceCategories(
                userNo,
                Constants.GroupCode.RECIPE_CATEGORY,
                Constants.GroupCode.PREFER_CATEGORY
        );

        return preferCategoryVoList.stream()
                .map(preferCategoryVo -> new PreferCategoryDto(
                        preferCategoryVo.getCategoryCode(),
                        preferCategoryVo.getCategoryName(),
                        preferCategoryVo.getStatusCode(),
                        preferCategoryVo.getStatusName(),
                        imageKitUtil.getUrl(preferCategoryVo.getIconUrl())
                                .orElseThrow(() -> new RuntimeException("잘못된 경로입니다 : " + preferCategoryVo.getIconUrl()))
                ))
                .toList();
    }

    @Transactional
    public Boolean changePreferenceCategoryStatus(String userNo, EditPreferCategoryDto editPreferCategoryDto) {
        recipeMapper.changePreferenceCategoryStatus(
                userNo,
                editPreferCategoryDto.getCategoryCode(),
                editPreferCategoryDto.getStatusCode()
        );

        return true;
    }

    public List<UserRecipeDto> getUserRecipes(String userNo, GetPageDto getPageDto) {
        List<SearchRecipeVo> recipeVoList = recipeMapper.getUserRecipes(
                userNo,
                (getPageDto.getPage() - 1) * getPageDto.getSize(),
                getPageDto.getSize(),
                Constants.Image.THUMBNAIL,
                Constants.GroupCode.DIFFICULTY,
                Constants.Recipe.RELEASE
        );

        return createUserRecipeListDto(recipeVoList);
    }

    public List<UserRecipeDto> getRecentViewRecipes(String userNo) {
        List<SearchRecipeVo> recipeVoList = recipeMapper.getRecentRecipes(
                userNo,
                Constants.Offset.RECENT_RECIPE_SIZE,
                Constants.Image.THUMBNAIL,
                Constants.GroupCode.DIFFICULTY,
                Constants.Recipe.RELEASE
        );

        return createUserRecipeListDto(recipeVoList);
    }

    public Boolean deleteRecipe(String userNo, String recipeNo) {
        if (!recipeMapper.isRecipeExists(recipeNo)) {
            throw new IllegalArgumentException("recipe.notFoundRecipe");
        }

        if (!recipeMapper.isRecipeAuthor(userNo, recipeNo)) {
            throw new IllegalArgumentException("recipe.invalidUser");
        }

        recipeMapper.updateRecipeStatus(userNo, recipeNo, Constants.Recipe.DELETED);

        return true;
    }

    public List<UserDraftRecipeDto> getDraftRecipes(String userNo) {
        List<UserDraftRecipeVo> userDraftRecipeVoList
                = recipeMapper.getDraftRecipes(userNo, Constants.Image.THUMBNAIL, Constants.GroupCode.DIFFICULTY, Constants.Recipe.DRAFT);

        return userDraftRecipeVoList.stream()
                .map(userDraftRecipeVo -> new UserDraftRecipeDto(
                        userDraftRecipeVo.getRecipeNo(),
                        userDraftRecipeVo.getName(),
                        userDraftRecipeVo.getDescription(),
                        getDraftRecipeImageUrl(userDraftRecipeVo.getImageUrl()),
                        userDraftRecipeVo.getCookingTime(),
                        userDraftRecipeVo.getDifficulty()
                ))
                .toList();
    }

    private String getDraftRecipeImageUrl(String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            return StringUtils.EMPTY;
        }

        return imageKitUtil.getUrl(imageUrl)
                .orElseThrow(() -> new RuntimeException("잘못된 경로입니다 : " + imageUrl));
    }

    private List<UserRecipeDto> createUserRecipeListDto(List<SearchRecipeVo> recipeVoList) {
        return recipeVoList.stream()
                .map(searchRecipeVo -> new UserRecipeDto(
                        searchRecipeVo.getRecipeNo(),
                        searchRecipeVo.getName(),
                        searchRecipeVo.getDescription(),
                        imageKitUtil.getUrl(searchRecipeVo.getImageUrl())
                                .orElseThrow(() -> new RuntimeException("잘못된 경로입니다 : " + searchRecipeVo.getImageUrl())),
                        searchRecipeVo.getCookingTime(),
                        searchRecipeVo.getDifficultyCodeName(),
                        searchRecipeVo.getLikeCount(),
                        searchRecipeVo.getIsLiked()
                ))
                .toList();
    }

    private RecipeDetailVo getRecipeDetailVo(String recipeNo, String userNo) {
        RecipeDetailVo recipeDetailVo = recipeMapper.getRecipeDetail(
                recipeNo,
                userNo,
                Constants.GroupCode.DIFFICULTY,
                Constants.GroupCode.RECIPE_STATUS,
                Constants.Image.THUMBNAIL,
                Constants.Image.COMPLETE
        );

        if (recipeDetailVo == null) {
            throw new IllegalArgumentException("recipe.notFoundRecipe");
        }

        return recipeDetailVo;
    }


    private List<IngredientDto> getIngredientDtoList(String recipeNo) {
        List<IngredientVo> ingredientVoList = recipeMapper.getIngredients(recipeNo);

        return ingredientVoList == null ? List.of()
                : ingredientVoList.stream()
                .map(vo -> new IngredientDto(
                        vo.getName(),
                        vo.getCategoryCode(),
                        vo.getQuantity(),
                        vo.getUnit(),
                        vo.getTip()
                ))
                .toList();
    }

    private RecipeDetailDto buildRecipeDetailDto(
            RecipeDetailVo recipeDetailVo,
            String mainImageUrl,
            List<String> completionImageUrls,
            List<IngredientDto> ingredientDtoList,
            List<StepDto> stepDtoList
    ) {
        return new RecipeDetailDto(
                recipeDetailVo.getNickname(),
                recipeDetailVo.getRecipeNo(),
                recipeDetailVo.getTitle(),
                recipeDetailVo.getDescription(),
                recipeDetailVo.getCookingTime(),
                recipeDetailVo.getServingSize(),
                recipeDetailVo.getDifficulty(),
                mainImageUrl,
                completionImageUrls,
                ingredientDtoList,
                stepDtoList,
                recipeDetailVo.getLikeCount(),
                recipeDetailVo.getLikeYn(),
                recipeDetailVo.getBookmarkYn(),
                recipeDetailVo.getReportYn(),
                recipeDetailVo.getStatusCode(),
                recipeDetailVo.getStatusName()
        );
    }

    private List<String> getCompletionImageUrls(RecipeDetailVo recipeDetailVo) {
        return recipeDetailVo.getCompletionImagePathList() == null ? List.of()
                : recipeDetailVo.getCompletionImagePathList().stream()
                .map(completionImagePath -> imageKitUtil.getUrl(completionImagePath)
                        .orElseThrow(() -> new RuntimeException("잘못된 경로입니다: " + completionImagePath)))
                .toList();
    }

    private List<StepDto> getStepDtoList(String recipeNo) {
        List<StepVo> stepVoList = recipeMapper.getSteps(recipeNo);

        return stepVoList == null ? List.of()
                : stepVoList.stream()
                .map(stepVo -> new StepDto(
                        stepVo.getContent(),
                        stepVo.getImagePathList().stream()
                                .map(stepImagePath -> imageKitUtil.getUrl(stepImagePath)
                                        .orElseThrow(() -> new RuntimeException("잘못된 경로입니다: " + stepImagePath)))
                                .toList()
                ))
                .toList();
    }

    private void insertDraftIngredients(String recipeNo, String userNo, List<DraftIngredientDto> ingredientList) {
        if (!ingredientList.isEmpty()) {
            recipeMapper.insertDraftIngredients(recipeNo, userNo, ingredientList);
        }
    }

    private void insertUploadIngredients(String recipeNo, String userNo, List<UploadIngredientDto> ingredientList) {
        if (!ingredientList.isEmpty()) {
            recipeMapper.insertUploadIngredients(recipeNo, userNo, ingredientList);
        }
    }

    private void insertImages(String recipeNo,
                              String userNo,
                              MultipartFile mainImage,
                              MultipartFile[] completionImages) throws Exception {
        int sortSequence = 0;

        if (mainImage != null && !mainImage.isEmpty()) {
            recipeMapper.insertRecipeImage(recipeNo, imageKitUtil.upload(mainImage), Constants.Image.THUMBNAIL, sortSequence, userNo);
        }

        if (completionImages != null) {
            for (MultipartFile completionImage : completionImages) {
                recipeMapper.insertRecipeImage(recipeNo, imageKitUtil.upload(completionImage), Constants.Image.COMPLETE, sortSequence++, userNo);
            }
        }
    }

    private void insertDraftSteps(String recipeNo,
                                  String userNo,
                                  DraftRecipeDto recipeInfo,
                                  MultipartFile[] stepImages) throws Exception {
        if (recipeInfo.getStepList().isEmpty()) {
            return;
        }

        int stepSequence = 0;

        for (DraftStepDto stepDto : recipeInfo.getStepList()) {
            InsertStepVo stepVo = new InsertStepVo(
                    null,
                    recipeNo,
                    stepDto.getContents(),
                    stepSequence++
            );

            recipeMapper.insertStep(stepVo, userNo);

            if (stepDto.getImageIndexes() == null || stepImages == null) {
                continue;
            }

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
        }
    }

    private void insertUploadSteps(String recipeNo,
                                   String userNo,
                                   UploadRecipeDto recipeInfo,
                                   MultipartFile[] stepImages) throws Exception {
        if (recipeInfo.getStepList().isEmpty()) {
            return;
        }

        int stepSequence = 0;

        for (UploadStepDto stepDto : recipeInfo.getStepList()) {
            InsertStepVo stepVo = new InsertStepVo(
                    null,
                    recipeNo,
                    stepDto.getContents(),
                    stepSequence++
            );

            recipeMapper.insertStep(stepVo, userNo);

            if (stepDto.getImageIndexes() == null || stepImages == null) {
                continue;
            }

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
        }
    }
}