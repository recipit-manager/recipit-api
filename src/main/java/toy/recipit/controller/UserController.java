package toy.recipit.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.dto.ApiResponse;
import toy.recipit.service.UserService;

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
            @NotBlank(message = "닉네임이 비어있습니다.")
            @Size(min = 2, max = 8, message = "올바른 닉네임 형식이 아닙니다.")
            String nickname) {
        String result = userService.isNicknameDuplicate(nickname) ? "Y" : "N";

        return ResponseEntity.ok(new ApiResponse<>("0000", "success", result));
    }
}
