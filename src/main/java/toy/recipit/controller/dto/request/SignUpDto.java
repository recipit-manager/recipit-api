package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Schema(description = "사용자의 회원가입 입력 정보")
public class SignUpDto {
    @NotBlank(message = "validation.firstName.blank")
    @Size(max = 10, message = "validation.firstName.size")
    @Schema(description = "성", example = "최")
    private final String firstName;

    @Size(max = 20, message = "validation.middleName.size")
    @Schema(description = "중간 이름 (생략 가능)")
    private final String middleName;

    @NotBlank(message = "validation.lastName.blank")
    @Size(max = 20, message = "validation.lastName.size")
    @Schema(description = "이름", example = "우빈")
    private final String lastName;

    @NotBlank(message = "validation.nickname.blank")
    @Size(min = 2, max = 8, message = "validation.nickname.size")
    @Pattern(regexp = "^[0-9A-Za-z가-힣]+$", message = "validation.nickname.pattern")
    @Schema(description = "닉네임", example = "우비빅")
    private final String nickname;

    @NotBlank(message = "validation.email.blank")
    @Email(message = "validation.email.pattern")
    @Size(max = 50, message = "validation.email.size")
    @Schema(description = "사용자 이메일", example = "example@gmail.com")
    private final String email;

    @NotBlank(message = "validation.password.blank")
    @Size(min = 8, max = 16, message = "validation.password.size")
    @Pattern.List({
            @Pattern(
                    regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\w\\s]).{8,16}$",
                    message = "validation.password.complexity"
            ),
            @Pattern(
                    regexp = "^(?!.*(.)\\1\\1).*$",
                    message = "validation.password.repetition"
            )
    })
    @Schema(description = "비밀번호", example = "Test1234@")
    private final String password;

    @NotBlank(message = "validation.countryCode.blank")
    @Schema(description = "국가 코드", example = "KO")
    private final String countryCode;

    @NotBlank(message = "validation.phoneNumber.blank")
    @Schema(description = "전화번호", example = "010-1234-5678")
    private final String phoneNumber;

    public SignUpDto(
            String firstName,
            String middleName,
            String lastName,
            String nickname,
            String email,
            String password,
            String countryCode,
            String phoneNumber
    ) {
        this.firstName = firstName;
        this.middleName = (middleName == null) ? StringUtils.EMPTY : middleName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
    }

}
