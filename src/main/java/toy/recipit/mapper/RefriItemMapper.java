package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.SearchRefriVo;
import toy.recipit.mapper.vo.UnMatchIngredientVo;

import java.util.List;

@Mapper
public interface RefriItemMapper {
    List<String> getAutoCompleteList(@Param("keyword") String keyword);

    List<SearchRefriVo> getRefriItemRecipeList(
            @Param("userNo") String userNo,
            @Param("keywordList") List<String> keywordList,
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("imageTypeCode") String imageTypeCode,
            @Param("difficultyGroupCode") String difficultyGroupCode
    );

    List<UnMatchIngredientVo> getUnMatchIngredients(
            @Param("recipeNos") List<String> recipeIds,
            @Param("keywordList") List<String> keywordList
    );
}
