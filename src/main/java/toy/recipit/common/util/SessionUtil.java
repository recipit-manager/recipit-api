package toy.recipit.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import toy.recipit.common.Constants;

import java.util.Optional;

@Component
public class SessionUtil {
    public void setSessionUserNo(HttpServletRequest request, String userNo) {
        request.getSession(true).setAttribute(Constants.SessionKey.USER, userNo);
    }

    public Optional<String> getSessionUserNo(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session == null) return Optional.empty();

        Object obj = session.getAttribute(Constants.SessionKey.USER);
        if (obj instanceof String userNo) {
            return Optional.of(userNo);
        }
        return Optional.empty();
    }
}