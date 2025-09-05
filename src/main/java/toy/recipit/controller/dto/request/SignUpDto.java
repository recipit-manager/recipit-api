package toy.recipit.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public class SignUpDto {
    @NotBlank(message = "validation.firstName.blank")
    @Size(max = 10, message = "validation.firstName.size")
    private final String firstName;

    @Size(max = 20, message = "validation.middleName.size")
    private final String middleName = StringUtils.EMPTY;

    @NotBlank(message = "validation.lastName.blank")
    @Size(max = 20, message = "validation.lastName.size")
    private final String lastName;

    @NotBlank(message = "validation.nickname.blank")
    @Size(min = 2, max = 8, message = "validation.nickname.size")
    @Pattern(regexp = "^[0-9A-Za-z가-힣]+$", message = "validation.nickname.pattern")
    private final String nickname;

    @NotBlank(message = "validation.email.blank")
    @Email(message = "validation.email.pattern")
    @Size(max = 50, message = "validation.email.size")
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
    private final String password;

    @NotBlank(message = "validation.groupCode.blank")
    private final String groupCode;

    @NotNull(message = "validation.countryCode.null")
    @Valid
    private final CommonCodeDto countryCode;

    @NotBlank(message = "validation.phoneNumber.blank")
    private final String phoneNumber;
}
