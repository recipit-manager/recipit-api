package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;

@Mapper
public interface UserMapper {
    boolean isNicknameDuplicate(@Param("nickname") String nickname);

    boolean checkExistsByEmail(@Param("email") String email);

    void insertEmailVerification(@Param("email") String email,
                                 @Param("code") String code);

    void updateEmailVerification(@Param("email") String email,
                                          @Param("code") String code);

    OffsetDateTime getPostDateTimeByEmail(@Param("email") String email);
}
