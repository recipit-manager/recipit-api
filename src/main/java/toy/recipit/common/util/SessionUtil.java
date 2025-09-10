package toy.recipit.common.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import toy.recipit.common.Constants;
import toy.recipit.controller.dto.response.SessionUser;

import java.util.Optional;

@Component
public class SessionUtil {
    public void setSessionUser(HttpSession session, SessionUser sessionUser) {
        session.setAttribute(Constants.SessionKey.USER, sessionUser);
    }

    public Optional<String> getSessionUserNo(HttpSession session) {
        if (session.getAttribute(Constants.SessionKey.USER) instanceof SessionUser user) {
            return Optional.ofNullable(user.getUserNo());
        }
        return Optional.empty();
    }
}