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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<String, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userNo = session.getAttributes().get("userNo").toString();

        if (userNo != null) {
            sessions.computeIfAbsent(userNo, k -> ConcurrentHashMap.newKeySet()).add(session);
            log.info("Success connect: {}", userNo);
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        sessions.forEach((userNo, webSocketSessionSet) -> {
            webSocketSessionSet.remove(session);

            if (webSocketSessionSet.isEmpty()) {
                sessions.remove(userNo);
            }
        });
    }

    public void sendMessage(String userNo, String msg) {
        Set<WebSocketSession> Sessions = sessions.get(userNo);

        if (Sessions.isEmpty()) {
            log.warn("{} Session not Fount or Close", userNo);
        }

        for (WebSocketSession session : Sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(msg));
                } catch (IOException e) {
                    log.warn("{} Fail Send Message: {}", userNo, e.getMessage(), e);
                }
            }
        }
    }
}
