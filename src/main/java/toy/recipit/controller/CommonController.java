package toy.recipit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.CountryCodeDto;
import toy.recipit.controller.dto.response.IngredientTypeDto;
import toy.recipit.controller.dto.response.RecipeCategoryDto;
import toy.recipit.controller.dto.response.ReportCategoryDto;
import toy.recipit.controller.dto.response.DifficultyDto;
import toy.recipit.controller.dto.response.IngredientCategoryDto;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;
import toy.recipit.service.CommonService;

import java.util.List;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Tag(name = "공통", description = "Recipit 서비스 구성에 필요한 주요 공통정보를 제공합니다.")
public class CommonController {

    private final CommonService commonService;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "국가코드 목록 조회", description = "Recipit서비스에서 지원하는 국가별 전화번호 식별코드와 전화번호 형식 목록을 제공합니다.")
    @Parameters({
        @Parameter(name = "language", description = "시스템 언어")
    })
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

    @GetMapping("/refri-item/ingredient/list")
    public ApiResponse<IngredientCategoryDto> getIngredientsByCategory() {
        return apiResponseFactory.success(commonService.getIngredientsByCategory());
    }
}
