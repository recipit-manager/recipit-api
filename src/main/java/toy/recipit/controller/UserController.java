package toy.recipit.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.common.Constants;
import toy.recipit.common.exception.SessionNotExistsException;
import toy.recipit.common.exception.UserStatusInactiveException;
import toy.recipit.common.exception.UserStatusLockException;
import toy.recipit.common.util.SessionUtil;
import toy.recipit.controller.dto.request.ChangeNicknameDto;
import toy.recipit.controller.dto.request.ChangePasswordDto;
import toy.recipit.controller.dto.request.ChangeTemporaryPasswordDto;
import toy.recipit.controller.dto.request.EmailDto;
import toy.recipit.controller.dto.request.FindUserIdDto;
import toy.recipit.controller.dto.request.FindUserPasswordDto;
import toy.recipit.controller.dto.request.LoginDto;
import toy.recipit.controller.dto.request.SignUpDto;
import toy.recipit.controller.dto.response.ApiResponse;
import toy.recipit.controller.dto.response.AutoLoginResultDto;
import toy.recipit.controller.dto.response.LoginResultDto;
import toy.recipit.controller.dto.response.NotificationDto;
import toy.recipit.controller.dto.response.SendEmailAuthenticationDto;
import toy.recipit.controller.dto.response.SessionUserInfo;
import toy.recipit.controller.dto.response.UserInfoDto;
import toy.recipit.controller.dto.response.factory.ApiResponseFactory;
import toy.recipit.service.EmailVerificationService;
import toy.recipit.service.NotificationService;
import toy.recipit.service.UserService;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final ApiResponseFactory apiResponseFactory;
    private final SessionUtil sessionUtil;
    private final NotificationService notificationService;

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
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "KO") String language,
            @RequestBody @Valid SignUpDto signUpDto
    ) {

        return ResponseEntity.ok(apiResponseFactory.success(userService.signUp(signUpDto, language)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Boolean>> login(
            @RequestBody @Valid LoginDto loginDto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.warn(request.getSession().getId());

        LoginResultDto loginResult = userService.login(loginDto);
        sessionUtil.setSessionUserInfo(request,
                new SessionUserInfo(
                        loginResult.getUserNo(),
                        loginResult.getUserNickname(),
                        loginResult.getUserStatusCode()
                ));

        if (loginDto.isAutoLogin()) {
            setAutoLoginCookie(response, loginResult.getAutoLoginToken());
        }

        return ResponseEntity.ok(apiResponseFactory.success(true));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout(
            HttpServletResponse response,
            HttpServletRequest request,
            @CookieValue(value = Constants.UserLogin.AUTO_LOGIN_COOKIE_NAME, required = false)
            String autoLoginToken
    ) {
        sessionUtil.removeSession(request);

        if (autoLoginToken != null) {
            if (userService.isExistAutoLoginTokenAndRemove(autoLoginToken)) {
                removeAutoLoginCookie(response);
            }
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
        if (sessionUtil.isSessionExists(request)) {
            SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

            if (userInfo.getUserStatusCode().equals(Constants.UserStatus.STOP)) {
                throw new UserStatusLockException();
            }

            return ResponseEntity.ok(apiResponseFactory.success(userInfo.getUserNickname()));
        }

        if (autoLoginToken != null) {
            AutoLoginResultDto autoLoginResult = userService.autoLogin(autoLoginToken);

            if (autoLoginResult.isNeedDeleteToken()) {
                removeAutoLoginCookie(response);
                throw new SessionNotExistsException();
            } else if (autoLoginResult.getUserStatusCode().equals(Constants.UserStatus.STOP)) {
                removeAutoLoginCookie(response);
                throw new UserStatusLockException();
            } else if (autoLoginResult.getUserStatusCode().equals(Constants.UserStatus.INACTIVE)) {
                removeAutoLoginCookie(response);
                throw new UserStatusInactiveException();
            } else {
                sessionUtil.setSessionUserInfo(
                        request,
                        new SessionUserInfo(
                                autoLoginResult.getUserNo(),
                                autoLoginResult.getUserNickname(),
                                autoLoginResult.getUserStatusCode()
                        ));

                setAutoLoginCookie(response, autoLoginResult.getAutoLoginToken());

                return ResponseEntity.ok(apiResponseFactory.success(autoLoginResult.getUserNickname()));
            }
        }

        throw new SessionNotExistsException();
    }

    @GetMapping("/id")
    public ResponseEntity<ApiResponse<String>> findUserId(
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "KO") String language,
            @Valid @ModelAttribute FindUserIdDto findUserIdDto
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(userService.findUserId(findUserIdDto, language)));
    }

    @PostMapping("/password/temporary")
    public ResponseEntity<ApiResponse<Boolean>> sendTemporaryPassword(
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "KO") String language,
            @RequestBody @Valid FindUserPasswordDto findUserPasswordDto
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(userService.sendTemporaryPassword(findUserPasswordDto, language)));
    }

    @PatchMapping("/password/temporary")
    public ResponseEntity<ApiResponse<Boolean>> changeTemporaryPassword(
            HttpServletRequest request,
            @RequestBody @Valid ChangeTemporaryPasswordDto changeTemporaryPasswordDto
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(userService.changeTemporaryPassword(userInfo.getUserNo(), changeTemporaryPasswordDto)));
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Boolean>> changePassword(
            HttpServletRequest request,
            @RequestBody @Valid ChangePasswordDto changePasswordDto
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(userService.changePassword(userInfo.getUserNo(), changePasswordDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfo(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(userService.getUserInfo(userInfo.getUserNo())));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<Boolean>> changeNickname(
            HttpServletRequest request,
            @RequestBody @Valid ChangeNicknameDto changeNicknameDto
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(userService.changeNickname(userInfo.getUserNo(), changeNicknameDto)));
    }

    @GetMapping("/notification/list")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getNotifications(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(notificationService.getNotifications(userInfo.getUserNo())));
    }

    @PatchMapping("/notification/list/read")
    public ResponseEntity<ApiResponse<Boolean>> readNotifications(
            HttpServletRequest request,
            @RequestBody
            @NotEmpty(message = "validation.notification_id_list.empty")
            List<String> notificationIdList
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(notificationService.readNotifications(userInfo.getUserNo(), notificationIdList)));
    }

    @GetMapping("/notification/unread/exists")
    public ResponseEntity<ApiResponse<Boolean>> isUnreadNotificationExists(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(notificationService.isUnreadNotificationExists(userInfo.getUserNo())));
    }


    private void setAutoLoginCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(Constants.UserLogin.AUTO_LOGIN_COOKIE_NAME, token)
                .domain("mockiwi.com")
                .sameSite("none")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(Constants.UserLogin.AUTO_LOGIN_EXPIRATION_DAYS))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void removeAutoLoginCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(Constants.UserLogin.AUTO_LOGIN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
