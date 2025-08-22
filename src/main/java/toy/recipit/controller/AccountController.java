package toy.recipit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.dto.ApiResponse;
import toy.recipit.service.AccountService;

@RestController
@RequestMapping("/user")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/nickname/{nickname}/duplicateYn")
    public ResponseEntity<ApiResponse<String>> checkDuplicate(@PathVariable String nickname) {
        if (!StringUtils.hasText(nickname) || nickname.length() < 2 || nickname.length() > 8) {
            return ResponseEntity.ok(new ApiResponse<String>().badRequest());
        }

        try {
            String result = accountService.isNicknameDuplicate(nickname) ? "Y" : "N";
            return ResponseEntity.ok(new ApiResponse<String>().success(result));
        } catch (CannotGetJdbcConnectionException e) {
            return ResponseEntity.ok(new ApiResponse<String>().dbConnectionFailed());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<String>().serverError());
        }
    }
}
