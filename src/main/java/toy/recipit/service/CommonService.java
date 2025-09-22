package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.common.exception.IngredientNotFoundException;
import toy.recipit.common.util.ImageKitUtil;
import toy.recipit.controller.dto.response.CountryCodeDto;
import toy.recipit.controller.dto.response.DifficultyDto;
import toy.recipit.controller.dto.response.IngredientCategoryDto;
import toy.recipit.controller.dto.response.IngredientGroupDto;
import toy.recipit.controller.dto.response.IngredientItemDto;
import toy.recipit.controller.dto.response.IngredientTypeDto;
import toy.recipit.controller.dto.response.RecipeCategoryDto;
import toy.recipit.controller.dto.response.ReportCategoryDto;
import toy.recipit.mapper.CommonMapper;
import toy.recipit.mapper.vo.CommonDetailCodeVo;
import toy.recipit.mapper.vo.CommonGroupCodeWithDetailsVo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
                        imageKitUtil.getUrl(CommonDetailCodeVo.getNote1())
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
        List<String> refriIngredientGroupCodes = Arrays.stream(Constants.GroupCode.RefriIngredientCategory.values())
                .map(Enum::name)
                .toList();

        List<CommonGroupCodeWithDetailsVo> commonCodeGroupWithDetailsVoList = commonMapper.getCommonCodeGroupsByGroupCodes(refriIngredientGroupCodes);

        if (commonCodeGroupWithDetailsVoList.isEmpty()) {
            throw new IngredientNotFoundException();
        }

        List<IngredientGroupDto> ingredientGroupDtoList = commonCodeGroupWithDetailsVoList.stream()
                .map(commonCodeGroupWithDetailsVo -> {
                    String ingredientCategoryName = commonCodeGroupWithDetailsVo.getGroupCodeName();

                    List<IngredientItemDto> ingredientItemDtoList =
                            commonCodeGroupWithDetailsVo.getCommonCodeDetailVoList().stream()
                                    .map(commonCodeDetailVo -> new IngredientItemDto(
                                            commonCodeDetailVo.getCodeName(),
                                            imageKitUtil.getUrl(commonCodeDetailVo.getNote1())
                                    ))
                                    .toList();

                    return new IngredientGroupDto(ingredientCategoryName, ingredientItemDtoList);
                })
                .toList();

        List<String> ingredientCategoryNameList = ingredientGroupDtoList.stream()
                .map(IngredientGroupDto::getCategoryName)
                .toList();

        return new IngredientCategoryDto(ingredientGroupDtoList, ingredientCategoryNameList);
    }

    public Optional<CountryCodeDto> getCountryCode(String groupCode, String countryCode) {
        return commonMapper.getCommonDetailCode(groupCode, countryCode)
                .map(commonDetailCodeVo -> new CountryCodeDto(
                        commonDetailCodeVo.getCode(),
                        commonDetailCodeVo.getCodeName(),
                        commonDetailCodeVo.getNote4(),
                        commonDetailCodeVo.getNote2(),
                        commonDetailCodeVo.getNote3()
                ));
    }
}
