package toy.recipit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.Constants;
import toy.recipit.common.Language;
import toy.recipit.controller.dto.CountryCodeDto;
import toy.recipit.controller.factory.ApiResponseFactory;
import toy.recipit.service.CommonService;
import toy.recipit.controller.dto.ApiResponse;
import java.util.List;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping("/country/list")
    public ApiResponse<List<CountryCodeDto>> getCountryCodes(
            @RequestParam(defaultValue = "KO") Language language
    ) {
        List<CountryCodeDto> countryCodes = commonService.getCountryCodes(language.getGroupCode());

        return apiResponseFactory.success(countryCodes);
    }

    @GetMapping("/emailDomain/list")
    public ApiResponse<List<String>> getEmails() {
        List<String> Emails = commonService.getEmails();

        return apiResponseFactory.success(Emails);
    }
}
