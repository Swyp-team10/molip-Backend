package org.example.shallweeatbackend.dto;

/**
 * OAuth2Response 인터페이스는 OAuth2 제공자로부터 받은 사용자 정보를 정의합니다.
 * 이 인터페이스는 다양한 제공자 (예: Kakao, Naver, Google 등)의 사용자 정보를 일관되게 처리하기 위한 메서드를 제공합니다.
 */
public interface OAuth2Response {

    // 제공자의 이름 반환 (예: kakao, naver, google 등)
    String getProvider();

    // 제공자로부터 발급된 고유 사용자 ID 반환
    String getProviderId();

    // 사용자 이름 반환
    String getName();

    // 사용자 이메일 반환
    String getEmail();
}
