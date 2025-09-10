package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.InsertUserVo;
import toy.recipit.mapper.vo.UserVo;

import java.util.Optional;


@Mapper
public interface UserMapper {
    boolean isNicknameDuplicate(@Param("nickname") String nickname);

    boolean isEmailExists(@Param("emailHashing") String emailHashing);

    boolean isNameAndPhoneNumberExists(@Param("firstName") String firstName,
                                          @Param("middleName") String middleName,
                                          @Param("lastName") String lastName,
                                          @Param("phoneNumberHashing") String phoneNumberHashing);

    void insertUser(@Param("user") InsertUserVo user);

    Optional<UserVo> getUserByEmail(@Param("emailHashing") String emailHashing);

    void increaseLoginFailCount(@Param("emailHashing") String emailHashing,
                                @Param("updateId") String updateId);

    void updateStatusCode(@Param("emailHashing") String emailHashing,
                          @Param("statusCode") String statusCode,
                          @Param("updateId") String updateId);

    void resetLoginFailCount(@Param("emailHashing") String emailHashing,
                             @Param("updateId") String updateId);
}
