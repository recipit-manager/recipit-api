package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.CommonCodeVo;

import java.util.List;

@Mapper
public interface CommonMapper {
    List<CommonCodeVo> getCommonDetailCodes(String groupCode);

    List<CommonCodeVo> getCommonDetailCodeByIngredientGroupCode(
            @Param("groupCodes") List<String> groupCodes
    );
}