package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.InsertUserVo;


@Mapper
public interface UserMapper {
    boolean isNicknameDuplicate(@Param("nickname") String nickname);

    boolean isEmailExists(@Param("emailHashing") String emailHashing);

    boolean isNameAndPhoneNumberExists(@Param("firstName") String firstName,
                                          @Param("middleName") String middleName,
                                          @Param("lastName") String lastName,
                                          @Param("phoneNumberHashing") String phoneNumberHashing);

    void insertUser(@Param("user") InsertUserVo user);
}
