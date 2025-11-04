package toy.recipit.websocket;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userNo = session.getAttributes().get("userNo").toString();

        if (userNo != null) {
            sessions.put(userNo, session);
            log.info("Success connect: {}", userNo);
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        sessions.values().remove(session);
    }

    public void sendMessage(String userNo, String msg) {
        WebSocketSession session = sessions.get(userNo);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                log.warn("{} Fail Send Message: {}", userNo, e.getMessage());
            }
        } else {
            log.warn("{} Session not Fount or Close", userNo);
        }
    }
}
