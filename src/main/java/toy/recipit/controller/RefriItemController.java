package toy.recipit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.controller.dto.request.AutoCompleteDto;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;
import toy.recipit.service.RefriItemService;

import java.util.List;

@RestController
@RequestMapping("/refri-item")
@RequiredArgsConstructor
public class RefriItemController {
    private final ApiResponseFactory apiResponseFactory;
    private final RefriItemService refriItemService;

    @GetMapping("/ingredient/auto-complete")
    public ResponseEntity<ApiResponse<List<String>>> getAutoCompleteList(
            @Valid @ModelAttribute AutoCompleteDto autoCompleteDto
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(refriItemService.getAutoCompleteList(autoCompleteDto.getKeyword())));
    }
}
