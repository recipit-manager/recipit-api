package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.CommonDetailCodeVo;
import toy.recipit.mapper.vo.CommonGroupCodeWithDetailsVo;

import java.util.List;

@Mapper
public interface CommonMapper {
    List<CommonDetailCodeVo> getCommonDetailCodes(String groupCode);

    List<CommonGroupCodeWithDetailsVo> getCommonCodeGroupsByGroupCodes(
            @Param("groupCodes") List<String> groupCodes
    );

}