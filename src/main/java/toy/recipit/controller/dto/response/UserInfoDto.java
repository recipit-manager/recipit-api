package toy.recipit.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "사용자 정보")
public class UserInfoDto {
    @Schema(description = "사용자 이메일", example = "example@gmail.com")
    final private String email;
    @Schema(description = "닉네임", example = "우비빅")
    final private String nickName;
    @Schema(description = "성", example = "최")
    final private String firstName;
    @Schema(description = "중간 이름 (생략 가능)")
    final private String middleName;
    @Schema(description = "이름", example = "우빈")
    final private String lastName;
    @Schema(description = "전화번호", example = "010-1234-5678")
    final private String phoneNumber;
    @Schema(description = "국가 코드", example = "KO")
    final private String countryCode;
}