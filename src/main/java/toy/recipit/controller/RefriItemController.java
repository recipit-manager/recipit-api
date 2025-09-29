package toy.recipit.controller;

import io.netty.util.internal.StringUtil;
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
public class RefriItemController {
    private final ApiResponseFactory apiResponseFactory;
    private final RefriItemService refriItemService;
    private final SessionUtil sessionUtil;

    @GetMapping("/ingredient/auto-complete")
    public ResponseEntity<ApiResponse<List<String>>> getAutoCompleteList(
            @RequestParam
            @NotBlank(message = "refriItem.keyword.blank")
            String keyword
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(refriItemService.getAutoCompleteList(keyword)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<RefriItemRecipeListDto>>> getRefriItemList(
            HttpServletRequest request,
            @Valid @ModelAttribute GetRefriItemListDto gregorySearchDto
    ) {
        String userNo = StringUtil.EMPTY_STRING;

        if (sessionUtil.isSessionExists(request)) {
            SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);
            userNo = userInfo.getUserNo();
        }

        return ResponseEntity.ok(apiResponseFactory.success(refriItemService.getRefriItemRecipes(userNo, gregorySearchDto)));
    }
}