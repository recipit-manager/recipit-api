package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.CmDetailCodeVo;

import java.util.List;

@Mapper
public interface CommonMapper {
    List<CmDetailCodeVo> getCommonDetailCodes(String groupCode);

    List<CmDetailCodeVo> getCommonDetailCodeByIngredientGroupCode(
            @Param("groupCodes") List<String> groupCodes
    );
}