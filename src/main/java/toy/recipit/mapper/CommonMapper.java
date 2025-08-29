package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.CommonCodeVo;
import toy.recipit.mapper.vo.CommonDetailCodeVo;

import java.util.List;

@Mapper
public interface CommonMapper {
    List<CommonDetailCodeVo> getCommonDetailCodes(String groupCode);

    List<CommonCodeVo> getCommonDetailCodeByIngredientGroupCode(
            @Param("groupCodes") List<String> groupCodes
    );
}