package toy.recipit.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.util.SessionUtil;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.PopularRecipeDto;
import toy.recipit.controller.dto.response.SessionUserInfo;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;
import toy.recipit.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;
    private final SessionUtil sessionUtil;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping("/popular/list")
    public ResponseEntity<ApiResponse<List<PopularRecipeDto>>> getPopularRecipes(
            HttpServletRequest request,
            @RequestParam
            @Min(value = 1, message = "Recipe.list.size.min")
            int size
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getPopularRecipes(userInfo.getUserNo(), size)));
    }
}
