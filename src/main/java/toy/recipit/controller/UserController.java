package toy.recipit.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.Constants;
import toy.recipit.common.exception.NotLoginStatusException;
import toy.recipit.common.exception.SessionNotExistsException;
import toy.recipit.common.util.SessionUtil;
import toy.recipit.controller.dto.request.EmailDto;
import toy.recipit.controller.dto.request.LoginDto;
import toy.recipit.controller.dto.request.SignUpDto;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.AutoLoginResult;
import toy.recipit.controller.dto.response.LoginResult;
import toy.recipit.controller.dto.response.SendEmailAuthenticationDto;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;
import toy.recipit.service.EmailVerificationService;
import toy.recipit.service.UserService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final ApiResponseFactory apiResponseFactory;
    private final SessionUtil sessionUtil;

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
            @RequestBody @Valid EmailDto emailDto
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(emailVerificationService.sendEmailVerificationCode(emailDto.getEmail())));
    }

    @GetMapping("/email/authentication/{verificationcode}")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailVerificationCode(
            @PathVariable("verificationcode")
            @Size(min = 8, max = 8, message = "validation.verification_code.size")
            @Pattern(regexp = "^[0-9A-Z]+$", message = "validation.verification_code.blank")
            String verificationCode,

            @RequestParam
            @NotBlank(message = "validation.email.blank")
            @Email(message = "validation.email.pattern")
            String email
    ) {

        return ResponseEntity.ok(apiResponseFactory.success(emailVerificationService.checkEmailVerificationCode(email, verificationCode)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> signUp(
            @RequestBody @Valid SignUpDto signUpDto
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(userService.signUp(signUpDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Boolean>> login(
            @RequestBody @Valid LoginDto loginDto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        LoginResult loginResult = userService.login(loginDto);
        sessionUtil.setSessionUserNo(request, loginResult.getUserNo());

        if (loginDto.isAutoLogin()) {
            setAutoLoginCookie(response, loginResult.getAutoLoginToken());
        }

        return ResponseEntity.ok(apiResponseFactory.success(true));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout(
            HttpServletRequest request,
            @CookieValue(value = Constants.UserLogin.AUTO_LOGIN_COOKIE_NAME, required = false)
            String autoLoginToken
    ) {
        sessionUtil.removeSession(request);

        if (autoLoginToken != null && !autoLoginToken.isEmpty()) {
            userService.removeAutoLoginToken(autoLoginToken);
        }

        return ResponseEntity.ok(apiResponseFactory.success(true));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Boolean>> refresh(
            HttpServletRequest request
    ) {
        request.getSession(false);

        return ResponseEntity.ok(apiResponseFactory.success(true));
    }

    @GetMapping("/login/status")
    public ResponseEntity<ApiResponse<String>> getLoginStatus(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue(value = Constants.UserLogin.AUTO_LOGIN_COOKIE_NAME, required = false)
            String autoLoginToken
    ) {
        if(sessionUtil.isSessionExists(request)) {
            Optional<String> userNo = sessionUtil.getSessionUserNo(request);

            if (userNo.isPresent()) {
                String userNickName = userService.getUserNickName(userNo.get());

                if(autoLoginToken != null) {
                    String newAutoLoginToken = userService.refreshAutoLoginToken(autoLoginToken);
                    setAutoLoginCookie(response, newAutoLoginToken);
                }

                return ResponseEntity.ok(apiResponseFactory.success(userNickName));
            }

            if(autoLoginToken == null) throw new NotLoginStatusException();
        }

        if(autoLoginToken != null) {
            AutoLoginResult autoLoginResult = userService.autoLogin(autoLoginToken);
            String newAutoLoginToken = userService.refreshAutoLoginToken(autoLoginToken);

            sessionUtil.setSessionUserNo(request, autoLoginResult.getUserNo());
            setAutoLoginCookie(response, newAutoLoginToken);

            return ResponseEntity.ok(apiResponseFactory.success(autoLoginResult.getUserNickname()));
        }

        throw new SessionNotExistsException();
    }

    private void setAutoLoginCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(Constants.UserLogin.AUTO_LOGIN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(Constants.UserLogin.AUTO_LOGIN_EXPIRATION_DAYS));
        response.addCookie(cookie);
    }

}
