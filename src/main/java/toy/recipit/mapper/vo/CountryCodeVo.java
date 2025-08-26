package toy.recipit.mapper.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CountryCodeVo {
    private String code;
    private String name;
    private String dialCode;
    private String format;
    private String regex;
}
