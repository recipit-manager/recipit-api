package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.PopularRecipeVo;

import java.util.List;

@Mapper
public interface RecipeMapper {
    List<PopularRecipeVo> getPopularRecipes(@Param("userNo") String userNo,
                                            @Param("size") int size,
                                            @Param("imageTypeCode") String imageTypeCode);
}