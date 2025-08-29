package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.common.exception.IngredientNotFoundException;
import toy.recipit.common.util.ImageKitUtil;
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
import toy.recipit.mapper.vo.CommonDetailCodeVo;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final CommonMapper commonMapper;
    private final ImageKitUtil imageKitUtil;

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
                .map(CommonDetailCodeVo::getCodeName)
                .toList();
    }

    public List<RecipeCategoryDto> getRecipeCategories() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.RECIPE_CATEGORY)
                .stream()
                .map(CommonDetailCodeVo -> new RecipeCategoryDto(
                        CommonDetailCodeVo.getCode(),
                        CommonDetailCodeVo.getCodeName(),
                        imageKitUtil.getUrl(CommonDetailCodeVo.getNote1(), 3600).orElse("")
                ))
                .toList();
    }

    public List<IngredientTypeDto> getIngredientTypes() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.INGREDIENT_TYPE)
                .stream()
                .map(CommonDetailCodeVo -> new IngredientTypeDto(
                        CommonDetailCodeVo.getCode(),
                        CommonDetailCodeVo.getCodeName()
                ))
                .toList();
    }

    public List<ReportCategoryDto> getReportCategories() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.REPORT_CATEGORY)
                .stream()
                .map(CommonDetailCodeVo -> new ReportCategoryDto(
                        CommonDetailCodeVo.getCode(),
                        CommonDetailCodeVo.getCodeName()
                ))
                .toList();
    }

    public List<DifficultyDto> getDifficulties() {
        return commonMapper.getCommonDetailCodes(Constants.GroupCode.DIFFICULTY)
                .stream()
                .map(CommonDetailCodeVo -> new DifficultyDto(
                        CommonDetailCodeVo.getCodeName(),
                        CommonDetailCodeVo.getCode()
                ))
                .toList();
    }

    public IngredientCategoryDto getIngredientsByCategory() {
        List<String> groupCodes = Arrays.stream(Constants.GroupCode.RefriIngredientCategory.values())
                .map(Constants.GroupCode.RefriIngredientCategory::getCode)
                .toList();

        if (groupCodes.isEmpty()) {
            throw new IllegalStateException();
        }

        List<CommonCodeVo> ingredients =
                commonMapper.getCommonDetailCodeByIngredientGroupCode(groupCodes);

        if (ingredients.isEmpty()) {
            throw new IngredientNotFoundException();
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
                                    imageKitUtil.getUrl(CommonCodeVo.getNote1(), 3600).orElse("")
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
