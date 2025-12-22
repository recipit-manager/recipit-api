package toy.recipit.controller;

import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.util.SessionUtil;
import toy.recipit.controller.dto.request.GetRefriItemListDto;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.RefriItemRecipeListDto;
import toy.recipit.controller.dto.response.SessionUserInfo;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;
import toy.recipit.service.RefriItemService;

import java.util.List;

@RestController
@RequestMapping("/refri-item")
@RequiredArgsConstructor
@Validated
@Tag(name = "냉템요리", description = "Recipit에서 냉템요리 서비스 관련 API 정보를 제공합니다.")
public class RefriItemController {
    private final ApiResponseFactory apiResponseFactory;
    private final RefriItemService refriItemService;
    private final SessionUtil sessionUtil;

    @Operation(summary = "재료명 자동완성", description = "사용자가 입력한 키워드의 자동원성 재료명 목록을 반환합니다.")
    @GetMapping("/ingredient/auto-complete")
    public ResponseEntity<ApiResponse<List<String>>> getAutoCompleteList(
            @RequestParam
            @NotBlank(message = "refriItem.keyword.blank")
            @Schema(description = "입력한 키워드")
            String keyword
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(refriItemService.getAutoCompleteList(keyword)));
    }

    @Operation(summary = "냉템요리 검색", description = "사용자가 입력한 재료 기반의 레시피 목록을 반환합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<RefriItemRecipeListDto>>> getRefriItemList(
            HttpServletRequest request,
            @Valid @ModelAttribute GetRefriItemListDto getRefriItemListDto
    ) {
        String userNo = StringUtil.EMPTY_STRING;

        if (sessionUtil.isSessionExists(request)) {
            SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);
            userNo = userInfo.getUserNo();
        }

        return ResponseEntity.ok(apiResponseFactory.success(refriItemService.getRefriItemRecipes(userNo, getRefriItemListDto)));
    }
}