package toy.recipit.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.request.EmailDto;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.SendEmailAuthenticationDto;
import toy.recipit.service.UserService;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;

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

    @PostMapping("/email/authentication")
    public ResponseEntity<ApiResponse<SendEmailAuthenticationDto>> sendEmailAuthentication(
            @RequestBody @Valid EmailDto authenticationEmail
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(userService.sendEmailVerificationCode(authenticationEmail.getEmail())));
    }
}
