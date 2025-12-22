package toy.recipit.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "사용자가 입력한 로그인 정보")
public class LoginDto {
    @NotBlank(message = "validation.email.blank")
    @Email(message = "validation.email.pattern")
    @Size(max = 50, message = "validation.email.size")
    @Schema(description = "이메일", example = "example@gmail.com")
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
    @Schema(description = "자동 로그인 체크 여부")
    private final boolean autoLogin;
}
