package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import toy.recipit.mapper.vo.CountryCodeVo;

import java.util.List;

@Mapper
public interface CommonMapper {
    List<CountryCodeVo> getCountryCodes(String language);
}
