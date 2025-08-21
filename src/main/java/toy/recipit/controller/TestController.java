package toy.recipit.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.recipit.service.TestService;

@RestController
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/now")
    public String test(
            HttpServletRequest request
    ) {
        request.getSession(true).setAttribute("Dummy","DumDum12");

        return testService.test();
    }
}
