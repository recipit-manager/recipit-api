package toy.recipit.controller;

import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import toy.recipit.common.util.SessionUtil;
import toy.recipit.controller.dto.request.DraftRecipeDto;
import toy.recipit.controller.dto.request.EditPreferCategoryDto;
import toy.recipit.controller.dto.request.GetPageDto;
import toy.recipit.controller.dto.request.GetRecipeListDto;
import toy.recipit.controller.dto.request.ReportRecipeDto;
import toy.recipit.controller.dto.request.UploadRecipeDto;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.BookmarkRecipeDto;
import toy.recipit.controller.dto.response.PopularRecipeDto;
import toy.recipit.controller.dto.response.PreferCategoryDto;
import toy.recipit.controller.dto.response.RecipeDetailDto;
import toy.recipit.controller.dto.response.RecipeListDto;
import toy.recipit.controller.dto.response.SessionUserInfo;
import toy.recipit.controller.dto.response.UserDraftRecipeDto;
import toy.recipit.controller.dto.response.UserRecipeDto;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;
import toy.recipit.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
@Tag(name = "레시피", description = "Recipit 서비스에서 레시피에 관련된 API입니다..")
public class RecipeController {
    private final RecipeService recipeService;
    private final SessionUtil sessionUtil;
    private final ApiResponseFactory apiResponseFactory;

    @Operation(summary = "인기 레시피 목록 조회", description = "최근 인기 있는 레시피 목록을 반환합니다.")
    @GetMapping("/popular/list")
    public ResponseEntity<ApiResponse<List<PopularRecipeDto>>> getPopularRecipes(
            HttpServletRequest request,
            @RequestParam
            @Min(value = 1, message = "recipe.list.size.min")
            @Schema(description = "조회할 레시피 수", example = "10")
            int size
    ) {
        String userNo = StringUtil.EMPTY_STRING;

        if(sessionUtil.isSessionExists(request)) {
            SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);
            userNo = userInfo.getUserNo();
        }

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getPopularRecipes(userNo, size)));
    }

    @Operation(summary = "임시저장 레시피 수 조회", description = "사용자가 임시저장한 레시피의 수를 반환합니다.")
    @GetMapping("/draft/count")
    public ResponseEntity<ApiResponse<Integer>> getDraftRecipeCount(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getDraftRecipeCount(userInfo.getUserNo())));
    }

    @Operation(summary = "레시피 좋아요", description = "레시피를 좋아요 설정합니다.")
    @PostMapping("/{recipeNo}/like")
    public ResponseEntity<ApiResponse<Boolean>> likeRecipe(
            HttpServletRequest request,
            @PathVariable
            @Schema(description = "좋아요 할 레시피 번호", example = "1001")
            String recipeNo
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.likeRecipe(userInfo.getUserNo(), recipeNo)));
    }

    @Operation(summary = "레시피 좋아요 취소", description = "레시피의 좋아요를 취소 합니다.")
    @DeleteMapping("/{recipeNo}/like")
    public ResponseEntity<ApiResponse<Boolean>> unlikeRecipe(
            HttpServletRequest request,
            @PathVariable
            @Schema(description = "좋아요 취소 할 레시피 번호", example = "1001")
            String recipeNo
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.unlikeRecipe(userInfo.getUserNo(), recipeNo)));
    }

    @Operation(summary = "최신순 레시피 조회", description = "최신순으로 정렬하여 레시피 목록을 반환합니다.")
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

    @Operation(summary = "좋아요순 레시피 조회", description = "좋아요순으로 정렬하여 레시피 목록을 반환합니다.")
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

    @Operation(summary = "레시피 임시저장", description = "작성 중이던 레시피를 임시저장합니다.")
    @PostMapping(value = "/draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Boolean>> saveDraftRecipe(
            HttpServletRequest request,
            @Valid @RequestPart DraftRecipeDto recipeInfo,
            @Schema(description = "레시피 대표 이미지")
            @RequestPart(required = false) MultipartFile mainImage,
            @Schema(description = "조리 단계 이미지 배열")
            @RequestPart(required = false) MultipartFile[] stepImages,
            @Schema(description = "완성 이미지 배열")
            @RequestPart(required = false) MultipartFile[] completionImages
    ) throws Exception {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.saveDraftRecipe(
                userInfo.getUserNo(),
                recipeInfo,
                mainImage,
                stepImages,
                completionImages
        )));
    }

    @Operation(summary = "레시피 업로드", description = "작성한 레시피를 업로드합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Boolean>> uploadRecipe(
            HttpServletRequest request,
            @Valid @RequestPart UploadRecipeDto recipeInfo,
            @Schema(description = "레시피 대표 이미지")
            @RequestPart MultipartFile mainImage,
            @Schema(description = "조리 단계 이미지 배열")
            @RequestPart(required = false) MultipartFile[] stepImages,
            @Schema(description = "완성 이미지 배열")
            @RequestPart(required = false) MultipartFile[] completionImages
    ) throws Exception{
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.uploadRecipe(
                userInfo.getUserNo(),
                recipeInfo,
                mainImage,
                stepImages,
                completionImages
        )));
    }

    @Operation(summary = "레시피 상세내용 조회", description = "레시피의 상세정보를 반환합니다.")
    @GetMapping("/{recipeNo}")
    public ResponseEntity<ApiResponse<RecipeDetailDto>> getRecipeDetail(
            @PathVariable("recipeNo")
            @Schema(description = "조회 할 레시피 번호", example = "1001")
            String recipeNo,
            HttpServletRequest request) {

        String userNo = StringUtil.EMPTY_STRING;

        if (sessionUtil.isSessionExists(request)) {
            SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);
            userNo = userInfo.getUserNo();
        }

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getRecipeDetail(recipeNo, userNo)));
    }

    @Operation(summary = "레시피 즐겨찾기", description = "레시피를 즐겨찾기 합니다.")
    @PostMapping("/{recipeNo}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> bookmarkRecipe(
            HttpServletRequest request,
            @PathVariable
            @Schema(description = "즐겨찾기 할 레시피 번호", example = "1001")
            String recipeNo
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.bookmarkRecipe(userInfo.getUserNo(), recipeNo)));
    }

    @Operation(summary = "레시피 즐겨찾기 취소", description = "레시피 즐겨찾기를 취소합니다.")
    @DeleteMapping("/{recipeNo}/bookmark")
    public ResponseEntity<ApiResponse<Boolean>> unBookmarkRecipe(
            HttpServletRequest request,
            @PathVariable
            @Schema(description = "즐겨찾기 취소 할 레시피 번호")
            String recipeNo
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.unBookmarkRecipe(userInfo.getUserNo(), recipeNo)));
    }

    @Operation(summary = "선호 카테고리 목록", description = "레시피 카테고리의 선호 상태 목록을 반환합니다.")
    @GetMapping("/preference-category/list")
    public ResponseEntity<ApiResponse<List<PreferCategoryDto>>> getPreferenceCategories(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getPreferenceCategories(userInfo.getUserNo())));
    }

    @Operation(summary = "카테고리 선호 상태 변경", description = "레시피 카테고리의 선호 상태를 변경합니다.")
    @PatchMapping("/preference-category/status")
    public ResponseEntity<ApiResponse<Boolean>> changePreferenceCategoryStatus(
            HttpServletRequest request,
            @RequestBody @Valid EditPreferCategoryDto editPreferCategoryDto
            ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.changePreferenceCategoryStatus(userInfo.getUserNo(), editPreferCategoryDto)));
    }

    @Operation(summary = "작성한 레시피 수 조회", description = "사용자가 작성한 레시피의 수를 반환합니다.")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getRecipeCount(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getRecipeCount(userInfo.getUserNo())));
    }

    @Operation(summary = "받은 좋아요 수 조회", description = "사용자가 받은 총 좋아요 수를 반환합니다.")
    @GetMapping("/like/count")
    public ResponseEntity<ApiResponse<Integer>> getUserLikeCount(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getUserLikeCount(userInfo.getUserNo())));
    }

    @Operation(summary = "즐겨찾기한 레시피 수 조회", description = "사용자가 즐겨찾기한 레시피 수를 반환합니다.")
    @GetMapping("/bookmark/count")
    public ResponseEntity<ApiResponse<Integer>> getUserBookmarkCount(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getUserBookmarkCount(userInfo.getUserNo())));
    }

    @Operation(summary = "작성한 레시피 목록 조회", description = "사용자가 작성한 레시피 목록을 반환합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<UserRecipeDto>>> getUserRecipes(
            HttpServletRequest request,
            @Valid @ModelAttribute GetPageDto getPageDto
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getUserRecipes(userInfo.getUserNo(), getPageDto)));
    }

    @Operation(summary = "레시피 삭제", description = "레시피를 삭제합니다.")
    @DeleteMapping("/{recipeNo}")
    public ResponseEntity<ApiResponse<Boolean>> deleteRecipe(
            HttpServletRequest request,
            @Schema(description = "삭제 할 레시피 번호", example = "1001")
            @PathVariable String recipeNo
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.deleteRecipe(userInfo.getUserNo(), recipeNo)));
    }

    @Operation(summary = "임시저장한 레시피 목록 조회", description = "임시저장한 레시피 목록을 반환합니다.")
    @GetMapping("/draft/list")
    public ResponseEntity<ApiResponse<List<UserDraftRecipeDto>>> getDraftRecipes(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getDraftRecipes(userInfo.getUserNo())));
    }

    @Operation(summary = "최근 열람한 레시피 목록 조회", description = "최근 열람한 레시피 목록을 반환합니다.")
    @GetMapping("recent/list")
    public ResponseEntity<ApiResponse<List<UserRecipeDto>>> getRecentRecipes(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getRecentViewRecipes(userInfo.getUserNo())));
    }

    @Operation(summary = "즐겨찾기한 레시피 목록 조회", description = "즐겨찾기한 레시피 목록을 반환합니다.")
    @GetMapping("/bookmark/list")
    public ResponseEntity<ApiResponse<List<BookmarkRecipeDto>>> getBookmarkRecipes(
            HttpServletRequest request,
            @Valid @ModelAttribute GetPageDto getPageDto
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.getBookmarkRecipes(userInfo.getUserNo(), getPageDto)));
    }

    @Operation(summary = "레시피 신고", description = "레시피를 신고합니다.")
    @PostMapping("/report/{recipeNo}")
    public ResponseEntity<ApiResponse<Boolean>> reportRecipe(
            HttpServletRequest request,
            @Schema(description = "신고할 레시피 번호", example = "1001")
            @PathVariable("recipeNo") String recipeNo,
            @Valid @RequestBody ReportRecipeDto reportRecipeDto
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(recipeService.reportRecipe(userInfo.getUserNo(), recipeNo, reportRecipeDto)));
    }
}
