package toy.recipit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.ApiResponse;
import toy.recipit.controller.dto.CountryCodeDto;
import toy.recipit.controller.dto.IngredientTypeDto;
import toy.recipit.controller.dto.RecipeCategoryDto;
import toy.recipit.controller.dto.ReportCategoryDto;
import toy.recipit.controller.dto.DifficultyDto;
import toy.recipit.controller.factory.ApiResponseFactory;
import toy.recipit.service.CommonService;

import java.util.List;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping("/country/list")
    public ApiResponse<List<CountryCodeDto>> getCountryCodes(
            @RequestParam(defaultValue = "KO") Constants.GroupCode.Language language
    ) {
        return apiResponseFactory.success(commonService.getCountryCodes(language.getGroupCode()));
    }

    @GetMapping("/email-domain/list")
    public ApiResponse<List<String>> getEmailDomains() {
        return apiResponseFactory.success(commonService.getEmailDomains());
    }

    @GetMapping("/recipe/category/list")
    public ApiResponse<List<RecipeCategoryDto>> getRecipeCategories() {
        return apiResponseFactory.success(commonService.getRecipeCategories());
    }

    @GetMapping("/ingredient-type/list")
    public ApiResponse<List<IngredientTypeDto>> getIngredientTypes() {
        return apiResponseFactory.success(commonService.getIngredientTypes());
    }

    @GetMapping("/report-category/list")
    public ApiResponse<List<ReportCategoryDto>> getReportCategories() {
        return apiResponseFactory.success(commonService.getReportCategories());
    }

    @GetMapping("/difficulty/list")
    public ApiResponse<List<DifficultyDto>> getDifficulties() {
        return apiResponseFactory.success(commonService.getDifficulties());
    }
}
