package toy.recipit.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CountryCodeDto {
    private final String code;
    private final String name;
    private final String dialCode;
    private final String format;
    private final String regex;
}
