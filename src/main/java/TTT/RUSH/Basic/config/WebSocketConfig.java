package TTT.RUSH.Basic.config;

import TTT.RUSH.Basic.service.PartyChatWebSocketService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new PartyChatWebSocketService(), "/chat/{partyId}")
                .setAllowedOrigins("*");
    }
}
