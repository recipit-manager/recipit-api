package toy.recipit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "회원", description = "Recipit 서비스의 회원정보 관련 API 정보를 제공합니다.")
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final ApiResponseFactory apiResponseFactory;
    private final SessionUtil sessionUtil;
    private final NotificationService notificationService;

    @Operation(summary = "닉네임 중복 검사", description = "입력한 닉네임이 이미 존재하는지 확입합니다.")
    @GetMapping("/nickname/{nickname}/duplicateYn")
    public ResponseEntity<ApiResponse<String>> checkNicknameDuplicate(
            @PathVariable
            @Size(min = 2, max = 8, message = "validation.nickname.size")
            @Pattern(regexp = "^[0-9A-Za-z가-힣]+$", message = "validation.nickname.pattern")
            @Schema(description = "검사할 닉네임", example = "우비빅")
            String nickname
    ) {
        String result = userService.isNicknameDuplicate(nickname) ? Constants.Yn.YES : Constants.Yn.NO;

        return ResponseEntity.ok(apiResponseFactory.success(result));
    }

    @Operation(summary = "이메일 인증코드 전송", description = "입력된 이메일 주소로 인증코드 메일을 전송합니다.")
    @PostMapping("/email/authentication")
    public ResponseEntity<ApiResponse<SendEmailAuthenticationDto>> sendEmailAuthentication(
            @RequestBody @Valid EmailDto emailDto
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(emailVerificationService.sendEmailVerificationCode(emailDto.getEmail())));
    }

    @Operation(summary = "이메일 인증코드 검증", description = "사용자가 입력한 인증코드가 올바른지 검증합니다.")
    @GetMapping("/email/authentication/{verificationcode}")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailVerificationCode(
            @PathVariable("verificationcode")
            @Size(min = 8, max = 8, message = "validation.verification_code.size")
            @Pattern(regexp = "^[0-9A-Z]+$", message = "validation.verification_code.blank")
            @Schema(description = "인증코드", example = "12345678")
            String verificationCode,

            @RequestParam
            @NotBlank(message = "validation.email.blank")
            @Email(message = "validation.email.pattern")
            @Schema(description = "이메일", example = "example@gmail.com")
            String email
    ) {

        return ResponseEntity.ok(apiResponseFactory.success(emailVerificationService.checkEmailVerificationCode(email, verificationCode)));
    }

    @Operation(summary = "회원가입", description = "사용자가 입력한 정보로 회원가입을 진행합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Boolean>> signUp(
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "KO") String language,
            @RequestBody @Valid SignUpDto signUpDto
    ) {

        return ResponseEntity.ok(apiResponseFactory.success(userService.signUp(signUpDto, language)));
    }

    @Operation(summary = "로그인", description = "입력한 이메일과 비밀번호로 로그인을 진행합니다.")
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

    @Operation(summary = "로그아웃", description = "로그인 된 사용자의 로그아웃을 진행합니다.")
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

    @Operation(summary = "세션 갱신", description = "세션의 만료시간을 갱신합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Boolean>> refresh(
            HttpServletRequest request
    ) {
        request.getSession(false);

        return ResponseEntity.ok(apiResponseFactory.success(true));
    }

    @Operation(summary = "로그인 상태 검사", description = "세션에 사용자 정보가 존재하는지 검사합니다. 존재하지 않는 경우 자동 로그인 토큰을 이용해 로그인을 시도합니다.")
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

    @Operation(summary = "아이디 찾기", description = "사용자의 이름과 전화번호를 이용해 이메일을 찾습니다.")
    @GetMapping("/id")
    public ResponseEntity<ApiResponse<String>> findUserId(
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "KO") String language,
            @Valid @ModelAttribute FindUserIdDto findUserIdDto
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(userService.findUserId(findUserIdDto, language)));
    }

    @Operation(summary = "비밀번호 찾기", description = "사용자의 이름, 전화번호, 이메일을 이용해 임시비밀번호를 전송합니다.")
    @PostMapping("/password/temporary")
    public ResponseEntity<ApiResponse<Boolean>> sendTemporaryPassword(
            @RequestHeader(value = "Accept-Language", required = false, defaultValue = "KO") String language,
            @RequestBody @Valid FindUserPasswordDto findUserPasswordDto
    ) {
        return ResponseEntity.ok(apiResponseFactory.success(userService.sendTemporaryPassword(findUserPasswordDto, language)));
    }

    @Operation(summary = "임시 비밀번호 변경", description = "임시비밀번호로 로그인한 사용자의 비밀번호를 변경합니다.")
    @PatchMapping("/password/temporary")
    public ResponseEntity<ApiResponse<Boolean>> changeTemporaryPassword(
            HttpServletRequest request,
            @RequestBody @Valid ChangeTemporaryPasswordDto changeTemporaryPasswordDto
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(userService.changeTemporaryPassword(userInfo.getUserNo(), changeTemporaryPasswordDto)));
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Boolean>> changePassword(
            HttpServletRequest request,
            @RequestBody @Valid ChangePasswordDto changePasswordDto
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(userService.changePassword(userInfo.getUserNo(), changePasswordDto)));
    }

    @Operation(summary = "사용자 정보 조회", description = "현재 로그인 된 사용자의 정보를 반환합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfo(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(userService.getUserInfo(userInfo.getUserNo())));
    }

    @Operation(summary = "닉네임 변경", description = "사용자의 닉네임을 변경합니다.")
    @PatchMapping("/nickname")
    public ResponseEntity<ApiResponse<Boolean>> changeNickname(
            HttpServletRequest request,
            @RequestBody @Valid ChangeNicknameDto changeNicknameDto
    ) throws Exception {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        if (userService.changeNickname(userInfo.getUserNo(), changeNicknameDto)) {
            SessionUserInfo changeUserInfo = new SessionUserInfo(
                    userInfo.getUserNo(),
                    changeNicknameDto.getNickname(),
                    Constants.UserStatus.ACTIVE
            );

            sessionUtil.setSessionUserInfo(request, changeUserInfo);

            return ResponseEntity.ok(apiResponseFactory.success(true));
        }

       throw new Exception();
    }

    @Operation(summary = "알림 목록 조회", description = "사용자가 받은 알림 목록을 반환합니다.")
    @GetMapping("/notification/list")
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getNotifications(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(notificationService.getNotifications(userInfo.getUserNo())));
    }

    @Operation(summary = "알림 읽음 처리", description = "전달받은 알림들을 읽음 상태로 변경합니다.")
    @PatchMapping("/notification/list/read")
    public ResponseEntity<ApiResponse<Boolean>> readNotifications(
            HttpServletRequest request,
            @RequestBody
            @NotEmpty(message = "validation.notification_id_list.empty")
            @Schema(description = "알림 번호 목록", example = "[1001, 1002]")
            List<String> notificationIdList
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(notificationService.readNotifications(userInfo.getUserNo(), notificationIdList)));
    }

    @Operation(summary = "읽지않은 알림 여부 조회", description = "읽지 않은 알림의 존재 여부를 반환합니다.")
    @GetMapping("/notification/unread/exists")
    public ResponseEntity<ApiResponse<Boolean>> isUnreadNotificationExists(
            HttpServletRequest request
    ) {
        SessionUserInfo userInfo = sessionUtil.getSessionUserInfo(request);

        return ResponseEntity.ok(apiResponseFactory.success(notificationService.isUnreadNotificationExists(userInfo.getUserNo())));
    }


    private void setAutoLoginCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(Constants.UserLogin.AUTO_LOGIN_COOKIE_NAME, token)
//                .domain("mockiwi.com") TODO: 도메인 설정
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
