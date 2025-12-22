package toy.recipit.controller.dto.request;

import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class FindUserPasswordDto {
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

    @NotBlank(message = "validation.countryCode.blank")
    @Schema(description = "국가 코드", example = "KO")
    private final String countryCode;

    @NotBlank(message = "validation.phoneNumber.blank")
    @Schema(description = "전화번호", example = "010-1234-5678")
    private final String phoneNumber;

    @NotBlank(message = "validation.email.blank")
    @Email(message = "validation.email.pattern")
    @Size(max = 50, message = "validation.email.size")
    @Schema(description = "사용자 이메일", example = "example@gmail.com")
    private final String email;

    public FindUserPasswordDto(
            String firstName,
            String middleName,
            String lastName,
            String countryCode,
            String phoneNumber,
            String email
    ) {
        this.firstName = firstName;
        this.middleName = (middleName == null) ? StringUtil.EMPTY_STRING : middleName;
        this.lastName = lastName;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
