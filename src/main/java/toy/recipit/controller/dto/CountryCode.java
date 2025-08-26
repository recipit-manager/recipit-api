package toy.recipit.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CountryCode {
    private String code;
    private String name;
    private String dialCode;
    private String format;
    private String regex;
}
