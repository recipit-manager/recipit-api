package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import toy.recipit.mapper.vo.cmDetailCodeVo;

import java.util.List;

@Mapper
public interface CommonMapper {
    List<cmDetailCodeVo> getCountryCodes(String language);
}
