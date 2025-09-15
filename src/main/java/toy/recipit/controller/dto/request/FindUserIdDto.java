package toy.recipit.controller.dto.request;

import io.netty.util.internal.StringUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class FindUserIdDto {
    @NotBlank(message = "validation.firstName.blank")
    @Size(max = 10, message = "validation.firstName.size")
    private final String firstName;

    @Size(max = 20, message = "validation.middleName.size")
    private final String middleName;

    @NotBlank(message = "validation.lastName.blank")
    @Size(max = 20, message = "validation.lastName.size")
    private final String lastName;

    @NotBlank(message = "validation.groupCode.blank")
    private final String groupCode;

    @NotBlank(message = "validation.code.blank")
    private final String code;

    @NotBlank(message = "validation.phoneNumber.blank")
    private final String phoneNumber;

    public FindUserIdDto(
            String firstName,
            String middleName,
            String lastName,
            String groupCode,
            String code,
            String phoneNumber
    ) {
        this.firstName = firstName;
        this.middleName = (middleName == null) ? StringUtil.EMPTY_STRING : middleName;
        this.lastName = lastName;
        this.groupCode = groupCode;
        this.code = code;
        this.phoneNumber = phoneNumber;
    }
}