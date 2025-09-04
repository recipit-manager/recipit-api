package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.UserVo;


@Mapper
public interface UserMapper {
    boolean isNicknameDuplicate(@Param("nickname") String nickname);

    boolean isExistsByEmailForSignUp(@Param("emailToken") String emailToken);

    boolean isExistsByNameAndPhoneForSignUp(@Param("firstName") String firstName,
                                          @Param("middleName") String middleName,
                                          @Param("lastName") String lastName,
                                          @Param("phoneNumberToken") String phoneNumberToken);

    void insertUser(@Param("user") UserVo user, @Param("insertId") String insertId);

}
