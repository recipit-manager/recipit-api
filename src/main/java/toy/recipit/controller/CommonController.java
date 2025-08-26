package toy.recipit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.controller.dto.CountryCode;
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
    public ApiResponse<List<CountryCode>> getCountryCodes(
            @RequestParam(name = "language", defaultValue = "ko") String language
    ) {
        List<CountryCode> countryCodes = commonService.getCountryCodes(language);

        return apiResponseFactory.success(countryCodes);
    }
}
