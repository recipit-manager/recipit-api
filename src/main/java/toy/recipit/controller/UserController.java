package toy.recipit.controller;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.dto.ApiResponse;
import toy.recipit.service.UserService;
import toy.recipit.common.ApiResult;
import toy.recipit.common.Constant;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/nickname/{nickname}/duplicateYn")
    public ResponseEntity<ApiResponse<String>> checkNicknameDuplicate(
            @PathVariable
            @Size(min = 2, max = 8, message = "{validation.nickname.size}")
            @Pattern(regexp = "^[0-9A-Za-z가-힣]+$", message = "{validation.nickname.pattern}")
            String nickname
    ) {
        String result = userService.isNicknameDuplicate(nickname) ? Constant.YES : Constant.NO;

        return ResponseEntity.ok(
                new ApiResponse<>(ApiResult.SUCCESS.getCode(), ApiResult.SUCCESS.getMessage(), result)
        );
    }
}
