package toy.recipit.controller;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.ApiResponse;
import toy.recipit.service.UserService;
import toy.recipit.controller.factory.ApiResponseFactory;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ApiResponseFactory apiResponseFactory;

    @GetMapping("/nickname/{nickname}/duplicateYn")
    public ResponseEntity<ApiResponse<String>> checkNicknameDuplicate(
            @PathVariable
            @Size(min = 2, max = 8, message = "validation.nickname.size")
            @Pattern(regexp = "^[0-9A-Za-z가-힣]+$", message = "validation.nickname.pattern")
            String nickname
    ) {
        String result = userService.isNicknameDuplicate(nickname) ? Constants.Yn.YES : Constants.Yn.NO;

        return ResponseEntity.ok(apiResponseFactory.success(result));
    }
}
