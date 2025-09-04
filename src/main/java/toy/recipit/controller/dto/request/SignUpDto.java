package toy.recipit.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @NotBlank(message = "validation.firstName.blank")
    @Size(max = 10, message = "validation.firstName.size")
    private String firstName;

    @Size(max = 20, message = "validation.middleName.size")
    @Builder.Default
    private String middleName = "";

    @NotBlank(message = "validation.lastName.blank")
    @Size(max = 20, message = "validation.lastName.size")
    private String lastName;

    @NotBlank(message = "validation.nickname.blank")
    @Size(min = 2, max = 8, message = "validation.nickname.size")
    @Pattern(regexp = "^[0-9A-Za-z가-힣]+$", message = "validation.nickname.pattern")
    private String nickname;

    @NotBlank(message = "validation.email.blank")
    @Email(message = "validation.email.pattern")
    @Size(max = 50, message = "validation.email.size")
    private String email;

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
    private String password;

    @NotBlank(message = "validation.verification_code.blank")
    @Pattern(regexp = "^[A-Z0-9]{8}$", message = "validation.verification_code.pattern")
    private String verificationCode;

    @NotBlank(message = "validation.countryCode.blank")
    private String countryCode;

    @NotBlank(message = "validation.phoneNumber.blank")
    private String phoneNumber;
}
