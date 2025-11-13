package toy.recipit.controller.dto.request;

import io.netty.util.internal.StringUtil;
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
    private final String firstName;

    @Size(max = 20, message = "validation.middleName.size")
    private final String middleName;

    @NotBlank(message = "validation.lastName.blank")
    @Size(max = 20, message = "validation.lastName.size")
    private final String lastName;

    @NotBlank(message = "validation.countryCode.blank")
    private final String countryCode;

    @NotBlank(message = "validation.phoneNumber.blank")
    private final String phoneNumber;

    @NotBlank(message = "validation.email.blank")
    @Email(message = "validation.email.pattern")
    @Size(max = 50, message = "validation.email.size")
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
