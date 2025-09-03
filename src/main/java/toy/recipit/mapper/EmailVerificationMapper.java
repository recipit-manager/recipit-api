package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import toy.recipit.mapper.vo.UserEmailVerification;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper
public interface EmailVerificationMapper {
    boolean isEmailExists(@Param("encryptEmail") String encryptEmail);

    void insertEmailVerification(@Param("encryptEmail") String encryptEmail,
                                 @Param("verificationCode") String verificationCode,
                                 @Param("statusCode") String statusCode,
                                 @Param("insertId") String insertId);

    void updateEmailVerification(@Param("encryptEmail") String encryptEmail,
                                 @Param("verificationCode") String verificationCode,
                                 @Param("statusCode") String statusCode,
                                 @Param("updateId") String updateId);

    LocalDateTime getEditDateTime(@Param("encryptEmail") String encryptEmail);

    Optional<UserEmailVerification> getUserEmailVerification(@Param("encryptEmail") String encryptEmail);

    void updateEmailVerificationStatus(@Param("encryptEmail") String encryptEmail,
                                                 @Param("statusCode") String statusCode,
                                                 @Param("updateId") String updateId);
}
