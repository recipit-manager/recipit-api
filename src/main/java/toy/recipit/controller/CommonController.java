package toy.recipit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.GroupCode;
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
            @RequestParam(defaultValue = "KO") GroupCode groupCode
    ) {
        List<CountryCodeDto> countryCodes = commonService.getCountryCodes(groupCode.getGroupCode());

        return apiResponseFactory.success(countryCodes);
    }

    @GetMapping("/emailDomain/list")
    public ApiResponse<List<String>> getEmailDomains() {
        List<String> emails = commonService.getEmailDomains();

        return apiResponseFactory.success(emails);
    }
}
