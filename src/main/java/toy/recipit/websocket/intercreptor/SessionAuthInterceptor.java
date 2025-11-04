package toy.recipit.websocket.intercreptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import toy.recipit.common.util.SessionUtil;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionAuthInterceptor implements HandshakeInterceptor {
    private final SessionUtil sessionUtil;

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response,
                                   @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servlet) {
            try {
                attributes.put("userNo", sessionUtil.getSessionUserInfo(servlet.getServletRequest()).getUserNo());

                return true;
            } catch (Exception e) {
                log.warn("Authentication failed: {}", e.getMessage(), e);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
            }
        }
        return false;
    }

    @Override public void afterHandshake(@NotNull ServerHttpRequest req, @NotNull ServerHttpResponse res,
                                         @NotNull WebSocketHandler wsHandler, Exception ex) {}
}
