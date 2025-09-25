package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.CommonDetailCodeVo;
import toy.recipit.mapper.vo.PopularRecipeVo;
import toy.recipit.mapper.vo.SearchRecipeVo;

import java.util.List;

@Mapper
public interface RecipeMapper {
    List<PopularRecipeVo> getPopularRecipes(@Param("userNo") String userNo,
                                            @Param("size") int size,
                                            @Param("imageTypeCode") String imageTypeCode);

    int getRecipeCount(@Param("userNo") String userNo,
                       @Param("statusCode") String statusCode);

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

}