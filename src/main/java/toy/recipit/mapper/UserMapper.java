package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.insertUserVo;


@Mapper
public interface UserMapper {
    boolean isNicknameDuplicate(@Param("nickname") String nickname);

    boolean isEmailExists(@Param("emailHashing") String emailHashing);

    boolean isNameAndPhoneExists(@Param("firstName") String firstName,
                                          @Param("middleName") String middleName,
                                          @Param("lastName") String lastName,
                                          @Param("phoneNumberHashing") String phoneNumberHashing);

    void insertUser(@Param("user") insertUserVo user, @Param("insertId") String insertId);

}
