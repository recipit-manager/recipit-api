package toy.recipit.controller;

import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import toy.recipit.common.util.SessionUtil;
import toy.recipit.controller.dto.request.DraftRecipeDto;
import toy.recipit.controller.dto.request.GetRecipeListDto;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.PopularRecipeDto;
import toy.recipit.controller.dto.response.RecipeListDto;
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
            @Min(value = 1, message = "recipe.list.size.min")
            int size
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getPopularRecipes(userInfo.getUserNo(), size)));
    }

    @GetMapping("/draft/count")
    public ResponseEntity<ApiResponse<Integer>> getDraftRecipeCount(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getDraftRecipeCount(userInfo.getUserNo())));
    }

    @PostMapping("/{recipeNo}/like")
    public ResponseEntity<ApiResponse<Boolean>> likeRecipe(
            HttpServletRequest request,
            @PathVariable
            String recipeNo
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.likeRecipe(userInfo.getUserNo(), recipeNo)));
    }

    @DeleteMapping("/{recipeNo}/like")
    public ResponseEntity<ApiResponse<Boolean>> unlikeRecipe(
            HttpServletRequest request,
            @PathVariable
            String recipeNo
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.unlikeRecipe(userInfo.getUserNo(), recipeNo)));
    }

    @GetMapping("/list/recent-order")
    public ResponseEntity<ApiResponse<RecipeListDto>> getRecentRecipeList(
            HttpServletRequest request,
            @Valid @ModelAttribute GetRecipeListDto requestDto
    ) {
        String userNo = StringUtil.EMPTY_STRING;

        if(sessionUtil.isSessionExists(request)) {
            SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);
            userNo = userInfo.getUserNo();
        }

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getRecentRecipes(userNo, requestDto)));
    }

    @GetMapping("/list/like-order")
    public ResponseEntity<ApiResponse<RecipeListDto>> getLikeRecipeList(
            HttpServletRequest request,
            @Valid @ModelAttribute GetRecipeListDto requestDto
    ) {
        String userNo = StringUtil.EMPTY_STRING;

        if(sessionUtil.isSessionExists(request)) {
            SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);
            userNo = userInfo.getUserNo();
        }

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getLikeRecipes(userNo, requestDto)));
    }

    @PostMapping(value = "/draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Boolean>> saveDraftRecipe(
            HttpServletRequest request,
            @Valid @RequestPart DraftRecipeDto recipeInfo,
            @RequestPart(required = false) MultipartFile mainImage,
            @RequestPart(required = false) MultipartFile[] stepImages,
            @RequestPart(required = false) MultipartFile[] completionImages
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.saveDraftRecipe(
                userInfo.getUserNo(),
                recipeInfo,
                mainImage,
                stepImages,
                completionImages
        )));
    }
}
