package toy.recipit.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CountryCode {
    private final String code;
    private final String name;
    private final String dialCode;
    private final String format;
    private final String regex;
}
