package toy.recipit.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import toy.recipit.websocket.intercreptor.SessionAuthInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler noticeWebSocketHandler;
    private final SessionAuthInterceptor sessionAuthInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(noticeWebSocketHandler, "/ws/notice")
                .addInterceptors(sessionAuthInterceptor)
                .setAllowedOrigins("*");
    }
}
