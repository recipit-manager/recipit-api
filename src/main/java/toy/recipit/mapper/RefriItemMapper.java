package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RefriItemMapper {
    List<String> getAutoCompleteList(@Param("keyword") String keyword);
}
