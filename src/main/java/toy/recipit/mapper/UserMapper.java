package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.UserVo;


@Mapper
public interface UserMapper {
    boolean isNicknameDuplicate(@Param("nickname") String nickname);

    boolean isEmailExists(@Param("emailHasing") String emailHasing);

    boolean isNameAndPhoneExists(@Param("firstName") String firstName,
                                          @Param("middleName") String middleName,
                                          @Param("lastName") String lastName,
                                          @Param("phoneNumberHasing") String phoneNumberHasing);

    void insertUser(@Param("user") UserVo user, @Param("insertId") String insertId);

}
