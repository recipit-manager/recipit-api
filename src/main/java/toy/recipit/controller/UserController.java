package toy.recipit.controller;

import jakarta.validation.constraints.NotBlank;
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
            @Size(min = 2, max = 8, message = "2~8자여야 합니다.")
            @Pattern(
                    regexp = "^[0-9A-Za-z가-힣]+$",
                    message = "숫자, 영문, 한글만 사용할 수 있습니다."
            )
            String nickname
    ) {
        String result = userService.isNicknameDuplicate(nickname) ? "Y" : "N";

        return ResponseEntity.ok(new ApiResponse<>("0000", "success", result));
    }
}
