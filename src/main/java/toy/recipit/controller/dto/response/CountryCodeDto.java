package toy.recipit.controller.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public class CountryCodeDto {
    private String empty = StringUtils.EMPTY;
    @NotBlank(message = "validation.countryCode.blank")
    private final String code;
    private final String name;
    private final String dialCode;
    private final String format;
    private final String regex;

    public CountryCodeDto(String code) {
        this.code = code;
        this.name = empty;
        this.dialCode = empty;
        this.format = empty;
        this.regex = empty;
    }
}
