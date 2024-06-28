package org.example.shallweeatbackend.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * CustomOAuth2User 클래스는 사용자 인증 정보를 나타내는 클래스입니다.
 * OAuth2User 인터페이스를 구현하여 사용자 정보를 제공합니다.
 */
public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO; // 사용자 정보를 담고 있는 DTO 객체

    // 사용자 DTO 객체 초기화
    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    // 사용자 속성 정보 반환 (사용 X)
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 사용자 권한 정보 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });

        return collection;
    }

    // 사용자 이름 반환
    @Override
    public String getName() {
        return userDTO.getName();
    }

    // 사용자 이메일 반환
    public String getEmail() {
        return userDTO.getEmail();
    }

    // 제공자 이름과, ID를 조합한 고유 식별자 반환
    public String getProviderId() {
        return userDTO.getProviderId();
    }
}
