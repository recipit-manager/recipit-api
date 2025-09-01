package toy.recipit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface EmailVerificationMapper {
    boolean isEmailExists(@Param("email") String email);

    void insertEmailVerification(@Param("email") String email,
                                 @Param("code") String code);

    void updateEmailVerification(@Param("email") String email,
                                 @Param("code") String code);

    LocalDateTime getEditDateTime(@Param("email") String email);
}
