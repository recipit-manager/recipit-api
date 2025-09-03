package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.UserEmailVerification;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper
public interface EmailVerificationMapper {
    boolean isEmailExists(@Param("hashingEmail") String hashingEmail);

    void insertEmailVerification(@Param("hashingEmail") String hashingEmail,
                                 @Param("verificationCode") String verificationCode,
                                 @Param("statusCode") String statusCode,
                                 @Param("insertId") String insertId);

    void updateEmailVerification(@Param("hashingEmail") String hashingEmail,
                                 @Param("verificationCode") String verificationCode,
                                 @Param("statusCode") String statusCode,
                                 @Param("updateId") String updateId);

    LocalDateTime getEditDateTime(@Param("hashingEmail") String hashingEmail);

    Optional<UserEmailVerification> getUserEmailVerification(@Param("hashingEmail") String hashingEmail);

    void updateEmailVerificationStatus(@Param("hashingEmail") String hashingEmail,
                                                 @Param("statusCode") String statusCode,
                                                 @Param("updateId") String updateId);
}
