package com.be.sportizebe.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독(브로드캐스트) prefix
        registry.enableSimpleBroker("/topic");

        // 클라이언트가 서버로 메시지 보낼 떄 prefix
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ws endpoint
        // < 일단 이건 테스트/호환성 때문에 주석처리 해놓음 > 순수 WebSocket임
         registry.addEndpoint("/ws")
                 .setAllowedOriginPatterns("*");

        // SockJS 사용은 다음과 같음
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
