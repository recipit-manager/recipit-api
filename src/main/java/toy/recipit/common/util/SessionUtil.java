package toy.recipit.common.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import toy.recipit.common.Constants;
import toy.recipit.common.exception.NotLoginStatusException;
import toy.recipit.common.exception.SessionNotExistsException;
import toy.recipit.common.exception.UserNotFoundException;
import toy.recipit.controller.dto.response.SessionUserInfo;

import java.util.Optional;

@Component
public class SessionUtil {
    public void setSessionUserInfo(HttpServletRequest request, SessionUserInfo userInfo) {
        request.getSession(true).setAttribute(Constants.SessionKey.USER, userInfo);
    }

    public Optional<SessionUserInfo> getSessionUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new SessionNotExistsException();
        }

        Object obj = session.getAttribute(Constants.SessionKey.USER);
        if (obj instanceof SessionUserInfo userInfo) {
            return Optional.of(userInfo);
        }

        throw new NotLoginStatusException();
    }

    public void removeSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isSessionExists(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null;
    }
}