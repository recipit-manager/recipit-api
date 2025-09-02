package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserMapper {
    boolean isNicknameDuplicate(@Param("nickname") String nickname);
}
