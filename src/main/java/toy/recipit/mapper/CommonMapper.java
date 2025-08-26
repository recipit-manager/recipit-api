package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import toy.recipit.controller.dto.CountryCodeDto;
import java.util.List;

@Mapper
public interface CommonMapper {
    List<CountryCodeDto> getCountryCodes(String language);
}
