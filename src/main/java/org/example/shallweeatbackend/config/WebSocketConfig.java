package org.example.shallweeatbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // enableSimpleBroker: 메시지를 구독한 클라이언트에게 메시지를 전송하는 간단한 메시지 브로커를 활성화
        // "/topic/vote"로 시작하는 경로를 구독한 클라이언트에게 메시지를 브로드캐스트함
        config.enableSimpleBroker("/topic/vote");

        // setApplicationDestinationPrefixes: 클라이언트가 서버로 메시지를 보낼 때 사용하는 경로의 접두사 설정
        // 현재 구조에서는 클라이언트가 WebSocket을 통해 서버로 데이터를 보내지 않으므로 필요하지 않음
        config.setApplicationDestinationPrefixes("/app"); // 현재는 필요하지 않은 설정, 제거 가능
    }

    // WebSocket 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket에 연결할 때 사용하는 엔드포인트 "/ws/vote" 설정
        // 클라이언트는 이 경로로 연결 요청을 보낼 수 있음
        registry.addEndpoint("/ws/vote")
                // setAllowedOriginPatterns: CORS 설정으로, 모든 도메인에서 연결을 허용함 ("*" 사용)
                // 실제 운영 환경에서는 특정 도메인만 허용하는 것이 바람직함
                .setAllowedOriginPatterns("*");
                // withSockJS: SockJS를 활성화하여 WebSocket을 지원하지 않는 브라우저에서도 폴백 메커니즘을 통해 통신 가능
//                .withSockJS();
    }
}
