package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.controller.dto.request.DraftIngredientDto;
import toy.recipit.controller.dto.request.UploadIngredientDto;
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

@Mapper
public interface RecipeMapper {
    List<PopularRecipeVo> getPopularRecipes(@Param("userNo") String userNo,
                                            @Param("size") int size,
                                            @Param("imageTypeCode") String imageTypeCode);

    int getRecipeCount(@Param("userNo") String userNo,
                       @Param("statusCode") String statusCode);

    int getUserLikeCount(@Param("userNo") String userNo,
                     @Param("statusCode") String statusCode);

    int getUserBookmarkCount(@Param("userNo") String userNo);

    Boolean isRecipeExists(@Param("recipeNo") String recipeNo);

    Boolean isLikeExists(@Param("userNo") String userNo,
                         @Param("recipeNo") String recipeNo);

    void updateLike(@Param("userNo") String userNo,
                    @Param("recipeNo") String recipeNo,
                    @Param("likeYn") String likeYn);

    void insertLike(@Param("userNo") String userNo,
                    @Param("recipeNo") String recipeNo,
                    @Param("likeYn") String likeYn);

    List<SearchRecipeVo> getRecipes(@Param("userNo") String userNo,
                                    @Param("categoryCode") String categoryCode,
                                    @Param("keyword") String keyword,
                                    @Param("offset") int offset,
                                    @Param("size") int size,
                                    @Param("imageTypeCode") String imageTypeCode,
                                    @Param("difficultyGroupCode") String difficultyGroupCode,
                                    @Param("sortType") String sortType);

    List<CommonDetailCodeVo> getRecipeCategories(@Param("keyword") String keyword,
                                                @Param("categoryGroupCode") String categoryGroupCode);

    void insertRecipe(InsertRecipeVo recipe);

    void insertDraftIngredients(@Param("recipeNo") String recipeNo,
                           @Param("userNo") String userNo,
                           @Param("ingredientList") List<DraftIngredientDto> ingredientList);

    void insertUploadIngredients(@Param("recipeNo") String recipeNo,
                                @Param("userNo") String userNo,
                                @Param("ingredientList") List<UploadIngredientDto> ingredientList);

    void insertRecipeImage(@Param("recipeNo") String recipeNo,
                           @Param("url") String url,
                           @Param("typeCode") String typeCode,
                           @Param("sortSequence") int sortSequence,
                           @Param("userNo") String userNo);

    void insertStep(@Param("step") InsertStepVo stepVo,
                    @Param("userNo") String userNo);

    void insertStepImage(@Param("stepNo") String stepNo,
                         @Param("url") String url,
                         @Param("sortSequence") int sortSequence,
                         @Param("userNo") String userNo);

    RecipeDetailVo getRecipeDetail(@Param("recipeNo") String recipeNo,
                                   @Param("userNo") String userNo,
                                   @Param("difficultyGroupCode") String difficultyGroupCode,
                                   @Param("recipeStatusGroupCode") String recipeStatusGroupCode,
                                   @Param("thumbnailImageCode") String thumbnailImageCode,
                                   @Param("completeImageCode") String completeImageCode);

    List<IngredientVo> getIngredients(@Param("recipeNo") String recipeNo);

    List<StepVo> getSteps(@Param("recipeNo") String recipeNo);

    void insertBookmark(@Param("userNo") String userNo,
                        @Param("recipeNo") String recipeNo);

    void deleteBookmark(@Param("userNo") String userNo,
                        @Param("recipeNo") String recipeNo);

    boolean isPreferCategoriesExists(@Param("userNo") String userNo);

    void insertPreferCategories(@Param("userNo") String userNo,
                                       @Param("categoryGroupCode") String categoryGroupCode,
                                       @Param("defaultStatusCode") String defaultStatusCode);

    List<PreferCategoryVo> getPreferenceCategories(@Param("userNo") String userNo,
                                                   @Param("categoryGroupCode") String categoryGroupCode,
                                                   @Param("statusGroupCode") String statusGroupCode);

    void changePreferenceCategoryStatus(@Param("userNo") String userNo,
                                        @Param("categoryCode") String categoryCode,
                                        @Param("statusCode") String statusCode);

    List<SearchRecipeVo> getUserRecipes(@Param("userNo") String userNo,
                                        @Param("offset") int offset,
                                        @Param("size") int size,
                                        @Param("imageTypeCode") String imageTypeCode,
                                        @Param("difficultyGroupCode") String difficultyGroupCode,
                                        @Param("statusCode") String statusCode);

    void updateRecipeStatus(@Param("userNo") String userNo,
                      @Param("recipeNo") String recipeNo,
                      @Param("statusCode") String statusCode);

    List<UserDraftRecipeVo> getDraftRecipes(@Param("userNo") String userNo,
                                            @Param("imageTypeCode") String imageTypeCode,
                                            @Param("difficultyGroupCode") String difficultyGroupCode,
                                            @Param("statusCode") String statusCode);

    void upsertRecentRecipe(@Param("userNo") String userNo,
                            @Param("recipeNo") String recipeNo);

    List<SearchRecipeVo> getRecentRecipes(@Param("userNo") String userNo,
                                          @Param("size") int size,
                                          @Param("imageTypeCode") String imageTypeCode,
                                          @Param("difficultyGroupCode") String difficultyGroupCode,
                                          @Param("statusCode") String statusCode);

    boolean isRecipeAuthor(String userNo, String recipeNo);
}