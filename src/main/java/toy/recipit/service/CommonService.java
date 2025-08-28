package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.CountryCodeDto;
import toy.recipit.controller.dto.RecipeCategoryDto;
import toy.recipit.controller.dto.IngredientTypeDto;
import toy.recipit.controller.dto.DifficultyDto;
import toy.recipit.controller.dto.ReportCategoryDto;
import toy.recipit.controller.dto.IngredientGroupDto;
import toy.recipit.controller.dto.IngredientCategoryDto;
import toy.recipit.controller.dto.IngredientItemDto;
import toy.recipit.mapper.CommonMapper;
import toy.recipit.mapper.vo.CommonCodeVo;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonService {
    @Value("${app.base-url}")
    private String baseUrl;
    private final CommonMapper commonMapper;

    public List<CountryCodeDto> getCountryCodes(String groupCode) {
        return commonMapper.getCommonDetailCodes(groupCode)
                .stream()
                .map(vo -> new CountryCodeDto(
                        vo.getCode(),
                        vo.getCodeName(),
                        vo.getNote4(),
                        vo.getNote2(),
                        vo.getNote3()
                ))
                .toList();
    }

    public List<String> getEmailDomains() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.EMAIL_DOMAIN)
                .stream()
                .map(CommonCodeVo::getCodeName)
                .toList();
    }

    public List<RecipeCategoryDto> getRecipeCategories() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.RECIPE_CATEGORY)
                .stream()
                .map(vo -> new RecipeCategoryDto(
                        vo.getCode(),
                        vo.getCodeName(),
                        baseUrl + vo.getNote1()
                ))
                .toList();
    }

    public List<IngredientTypeDto> getIngredientTypes() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.INGREDIENT_TYPE)
                .stream()
                .map(vo -> new IngredientTypeDto(
                        vo.getCode(),
                        vo.getCodeName()
                ))
                .toList();
    }

    public List<ReportCategoryDto> getReportCategories() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.REPORT_CATEGORY)
                .stream()
                .map(vo -> new ReportCategoryDto(
                        vo.getCode(),
                        vo.getCodeName()
                ))
                .toList();
    }

    public List<DifficultyDto> getDifficulties() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.DIFFICULTY)
                .stream()
                .map(vo -> new DifficultyDto(
                        vo.getCodeName(),
                        vo.getCode()
                ))
                .toList();
    }

    public IngredientCategoryDto getIngredientCategories() {
        List<CommonCodeVo> queryResult = commonMapper.getCommonDetailCodeByIngredientGroupCode(Constants.GroupCode.REFRI_ALL_CODES);

        Map<String, List<CommonCodeVo>> byGroup = queryResult.stream()
                .collect(Collectors.groupingBy(CommonCodeVo::getGroupCode, Collectors.toList()));

        List<IngredientGroupDto> ingredientGroupList = byGroup.values().stream()
                .map(ingredient -> {
                    String categoryName = ingredient.get(0).getGroupName();
                    List<IngredientItemDto> ingredientList = ingredient.stream()
                            .map(vo -> new IngredientItemDto(vo.getCodeName(), baseUrl + vo.getNote1()))
                            .toList();
                    return new IngredientGroupDto(categoryName, ingredientList);
                })
                .toList();

        List<String> categoryList = ingredientGroupList.stream()
                .map(IngredientGroupDto::getCategoryName)
                .toList();

        return new IngredientCategoryDto(ingredientGroupList, categoryList);
    }
}
