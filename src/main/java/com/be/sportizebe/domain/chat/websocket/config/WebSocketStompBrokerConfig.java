package com.be.sportizebe.domain.chat.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 메세지 브로커 활성화
@RequiredArgsConstructor
public class WebSocketStompBrokerConfig implements WebSocketMessageBrokerConfigurer {


    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독(sub) : 접두사로 시작하는 메시지를 브로커가 처리하도록 설정합니다. 클라이언트는 이 접두사로 시작하는 주제를 구독하여 메시지를 받을 수 있습니다.
        // 예를 들어, 소켓 통신에서 사용자가 특정 메시지를 받기위해 "/sub"이라는 prefix 기반 메시지 수신을 위해 Subscribe합니다.
        // 구독(브로드캐스트) prefix
        registry.enableSimpleBroker("/sub"); // /sub/** 패턴의 모든 구독을 SimpleBroker가 자동 처리

        // 발행(pub) : 접두사로 시작하는 메시지는 @MessageMapping이 달린 메서드로 라우팅됩니다. 클라이언트가 서버로 메시지를 보낼 때 이 접두사를 사용합니다.
        // 예를 들어, 소켓 통신에서 사용자가 특정 메시지를 전송하기 위해 "/pub"라는 prefix 기반 메시지 전송을 위해 Publish 합니다.
        // 클라이언트가 서버로 메시지 보낼 떄 prefix
        registry.setApplicationDestinationPrefixes("/pub");
    }

    // 각각 특정 URL에 매핑되는 STOMP 엔드포인트를 등록하고, 선택적으로 SockJS 폴백 옵션을 활성화하고 구성합니다.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ws endpoint
        // < 일단 이건 테스트/호환성 때문에 주석처리 해놓음 > 순수 WebSocket임
         registry.addEndpoint("/ws-stomp")
                 .setAllowedOriginPatterns("*")
                 .withSockJS();
    }
}
