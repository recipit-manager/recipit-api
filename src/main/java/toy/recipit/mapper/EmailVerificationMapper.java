package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.UserEmailVerification;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper
public interface EmailVerificationMapper {
    boolean isEmailExists(@Param("email") String email);

    void insertEmailVerification(@Param("email") String email,
                                 @Param("verificationCode") String verificationCode,
                                 @Param("statusCode") String statusCode,
                                 @Param("insertId") String insertId);

    void updateEmailVerification(@Param("email") String email,
                                 @Param("verificationCode") String verificationCode,
                                 @Param("statusCode") String statusCode,
                                 @Param("updateId") String updateId);

    LocalDateTime getEditDateTime(@Param("email") String email);

    Optional<UserEmailVerification> getUserEmailVerification(@Param("email") String email);

    void updateEmailVerificationStatus(@Param("email") String email,
                                                 @Param("statusCode") String statusCode,
                                                 @Param("updateId") String updateId);
}
