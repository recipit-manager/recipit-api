package toy.recipit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.recipit.common.Constants;
import toy.recipit.common.util.ImageKitUtil;
import toy.recipit.controller.dto.request.GetRefriItemListDto;
import toy.recipit.controller.dto.response.RefriItemRecipeListDto;
import toy.recipit.mapper.RefriItemMapper;
import toy.recipit.mapper.vo.SearchRefriVo;
import toy.recipit.mapper.vo.UnMatchIngredientVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefriItemService {
    private final RefriItemMapper refriItemMapper;
    private final ImageKitUtil imageKitUtil;

    public List<String> getAutoCompleteList(String keyword) {
        return refriItemMapper.getAutoCompleteList(keyword);
    }

    public List<RefriItemRecipeListDto> getRefriItemRecipes(String userNo, GetRefriItemListDto getRefriItemListDto) {
        int offset = (getRefriItemListDto.getPage() - 1) * getRefriItemListDto.getSize();

        List<SearchRefriVo> recipeVoList = refriItemMapper.getRefriItemRecipeList(
                userNo,
                getRefriItemListDto.getKeywordList(),
                offset,
                getRefriItemListDto.getSize(),
                Constants.Image.THUMBNAIL,
                Constants.GroupCode.DIFFICULTY
        );

        List<String> recipeNoList = recipeVoList.stream()
                .map(SearchRefriVo::getId)
                .toList();

        List<UnMatchIngredientVo> unMatchIngredientList = refriItemMapper.getUnMatchIngredients(
                recipeNoList,
                getRefriItemListDto.getKeywordList()
        );

        Map<String, List<String>> unMatchIngredientMap = unMatchIngredientList.stream()
                .collect(Collectors.groupingBy(
                        UnMatchIngredientVo::getRecipeNo,
                        Collectors.mapping(UnMatchIngredientVo::getIngredient, Collectors.toList())
                ));

        return recipeVoList.stream()
                .map(searchRefriVo -> new RefriItemRecipeListDto(
                        searchRefriVo.getId(),
                        searchRefriVo.getName(),
                        searchRefriVo.getDescription(),
                        imageKitUtil.getUrl(searchRefriVo.getImageUrl())
                                .orElseThrow(() -> new RuntimeException("잘못된 경로입니다 : " + searchRefriVo.getImageUrl())),
                        searchRefriVo.getCookingTime(),
                        searchRefriVo.getServingSize(),
                        searchRefriVo.getDifficulty(),
                        searchRefriVo.getDifficultyCode(),
                        searchRefriVo.getLikeCount(),
                        searchRefriVo.getIsLiked(),
                        unMatchIngredientMap.getOrDefault(searchRefriVo.getId(), List.of())
                ))
                .toList();
    }
}
