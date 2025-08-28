package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.*;
import toy.recipit.mapper.CommonMapper;
import toy.recipit.mapper.vo.CmDetailCodeVo;
import java.util.Arrays;
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
                .map(CmDetailCodeVo::getCodeName)
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

        List<String> groupCodes = Arrays.stream(Constants.GroupCode.RefriItem.values())
                .map(Constants.GroupCode.RefriItem::getCode)
                .toList();

        List<CmDetailCodeVo> result = commonMapper.getCommonDetailCodeByIngredientGroupCode(groupCodes);

        Map<String, List<CmDetailCodeVo>> byGroup = result.stream()
                .collect(Collectors.groupingBy(CmDetailCodeVo::getGroupCode));

        List<IngredientGroupDto> ingredientGroupList = Arrays.stream(Constants.GroupCode.RefriItem.values())
                .filter(refriItem -> byGroup.containsKey(refriItem.getCode()))
                .map(refriItem -> {
                    List<IngredientItemDto> ingredientList = byGroup.get(refriItem.getCode()).stream()
                            .map(vo -> new IngredientItemDto(
                                    vo.getCodeName(),
                                    baseUrl + vo.getNote1()
                            ))
                            .toList();
                    return new IngredientGroupDto(refriItem.getName(), ingredientList);
                })
                .toList();

        List<String> categoryList = ingredientGroupList.stream()
                .map(IngredientGroupDto::getCategoryName)
                .toList();

        return new IngredientCategoryDto(ingredientGroupList, categoryList);
    }
}
