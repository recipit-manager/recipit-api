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

import java.util.*;
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
                .map(CommonCodeVo -> new CountryCodeDto(
                        CommonCodeVo.getCode(),
                        CommonCodeVo.getCodeName(),
                        CommonCodeVo.getNote4(),
                        CommonCodeVo.getNote2(),
                        CommonCodeVo.getNote3()
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
                .map(CommonCodeVo -> new RecipeCategoryDto(
                        CommonCodeVo.getCode(),
                        CommonCodeVo.getCodeName(),
                        baseUrl + CommonCodeVo.getNote1()
                ))
                .toList();
    }

    public List<IngredientTypeDto> getIngredientTypes() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.INGREDIENT_TYPE)
                .stream()
                .map(CommonCodeVo -> new IngredientTypeDto(
                        CommonCodeVo.getCode(),
                        CommonCodeVo.getCodeName()
                ))
                .toList();
    }

    public List<ReportCategoryDto> getReportCategories() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.REPORT_CATEGORY)
                .stream()
                .map(CommonCodeVo -> new ReportCategoryDto(
                        CommonCodeVo.getCode(),
                        CommonCodeVo.getCodeName()
                ))
                .toList();
    }

    public List<DifficultyDto> getDifficulties() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.DIFFICULTY)
                .stream()
                .map(CommonCodeVo -> new DifficultyDto(
                        CommonCodeVo.getCodeName(),
                        CommonCodeVo.getCode()
                ))
                .toList();
    }

    public IngredientCategoryDto getIngredientsByCategory() {
        List<CommonCodeVo> ingredients = commonMapper.getCommonDetailCodeByIngredientGroupCode(
                Constants.GroupCode.REFRI_ALL_CODES
        );

        if (ingredients.isEmpty()) {
            throw new NoSuchElementException();
        }

        Map<String, List<CommonCodeVo>> ingredientGroupByCategory = ingredients.stream()
                .collect(Collectors.groupingBy(
                        CommonCodeVo::getGroupCode,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<IngredientGroupDto> ingredientGroupList = ingredientGroupByCategory.values()
                .stream()
                .map(ingredientGroups -> {
                    String categoryName = ingredientGroups.get(0).getGroupCodeName();

                    List<IngredientItemDto> ingredientList = ingredientGroups.stream()
                            .map(CommonCodeVo -> new IngredientItemDto(
                                    CommonCodeVo.getCodeName(),
                                    baseUrl + CommonCodeVo.getNote1()
                            ))
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
