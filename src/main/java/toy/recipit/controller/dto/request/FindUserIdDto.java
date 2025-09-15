package toy.recipit.controller.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public class FindUserIdDto {
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private CommonCodeDto countryCode;
    private String phoneNumber;

    public FindUserIdDto(
            String firstName,
            String middleName,
            String lastName,
            String groupCode,
            String code,
            String phoneNumber
    ) {
        this.firstName = firstName;
        this.middleName = (middleName == null) ? StringUtils.EMPTY : middleName;
        this.lastName = lastName;
        this.countryCode = new CommonCodeDto(groupCode, code);
        this.phoneNumber = phoneNumber;
    }
}