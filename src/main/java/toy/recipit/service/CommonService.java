package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.CountryCodeDto;
import toy.recipit.controller.dto.RecipeCategoryDto;
import toy.recipit.controller.dto.IngredientTypeDto;
import toy.recipit.controller.dto.DifficultyDto;
import toy.recipit.controller.dto.ReportCategoryDto;
import toy.recipit.mapper.CommonMapper;
import toy.recipit.mapper.vo.CmDetailCodeVo;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

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
                        vo.getNote1()
                ))
                .toList();
    }

    public List<IngredientTypeDto> getIngredientTypes(String groupCode) {
        return commonMapper.getCommonDetailCodes(groupCode)
                .stream()
                .map(vo -> new IngredientTypeDto(
                        vo.getCode(),
                        vo.getCodeName()
                ))
                .toList();
    }

    public List<ReportCategoryDto> getReportCategories(String groupCode) {
        return commonMapper.getCommonDetailCodes(groupCode)
                .stream()
                .map(vo -> new ReportCategoryDto(
                        vo.getCode(),
                        vo.getCodeName()
                ))
                .toList();
    }

    public List<DifficultyDto> getDifficulties(String groupCode) {
        return commonMapper.getCommonDetailCodes(groupCode)
                .stream()
                .map(vo -> new DifficultyDto(
                        vo.getCodeName(),
                        vo.getCode()
                ))
                .toList();
    }
}
