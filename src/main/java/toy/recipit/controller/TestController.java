package toy.recipit.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.dto.ApiResponse;
import toy.recipit.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/now")
    public String test(HttpServletRequest request) {
        request.getSession(true).setAttribute("Dummy","DumDum");

        return testService.test();
    }

    @GetMapping("/user/nickname/{nickname}/duplicateYn")
    public ResponseEntity<ApiResponse<String>> checkDuplicate(@PathVariable String nickname) {
        if (!StringUtils.hasText(nickname) || nickname.length() < 2 || nickname.length() > 8) {
            return ResponseEntity.ok(new ApiResponse<String>().badRequest());
        }

        try {
            String result = testService.isNicknameDuplicate(nickname) ? "N" : "Y";
            return ResponseEntity.ok(new ApiResponse<String>().success(result));
        } catch (CannotGetJdbcConnectionException e) {
            return ResponseEntity.ok(new ApiResponse<String>().dbConnectionFailed());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<String>().serverError());
        }
    }
}
