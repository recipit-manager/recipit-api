package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import toy.recipit.controller.dto.CountryCode;
import java.util.List;

@Mapper
public interface CommonMapper {
    List<CountryCode> getCountryCodes(String groupCode);
}
