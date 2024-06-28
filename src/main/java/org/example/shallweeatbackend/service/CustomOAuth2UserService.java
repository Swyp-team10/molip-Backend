package org.example.shallweeatbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.KakaoResponse;
import org.example.shallweeatbackend.dto.OAuth2Response;
import org.example.shallweeatbackend.dto.UserDTO;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth2User 객체를 상위 클래스에서 로드
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 클라이언트 등록 ID를 가져옴 (예: "kakao")
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2Response 객체를 초기화
        OAuth2Response oAuth2Response = null;

        // 클라이언트 등록 ID가 "kakao"인 경우, KakaoResponse 객체를 생성하여 사용자 속성 설정
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            // 다른 클라이언트 등록 ID인 경우 null 반환
            return null;
        }

        // 제공자 ID와 고유 식별자를 조합하여 providerId 생성 (예: "kakao_1234567890")
        String providerId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        // providerId를 이용하여 기존 사용자 데이터를 DB에서 조회
        User existData = userRepository.findByProviderId(providerId);

        if (existData == null) {
            // 기존에 사용자가 존재하지 않는 경우, 새로운 사용자 생성
            User user = new User();
            user.setProviderId(providerId);
            user.setEmail(oAuth2Response.getEmail());
            user.setName(oAuth2Response.getName());
            user.setRole("ROLE_USER");

            // 새로운 사용자 정보 DB에 저장
            userRepository.save(user);

            // 새로운 사용자 정보를 UserDTO로 변환하여 반환
            UserDTO userDTO = new UserDTO();
            userDTO.setProviderId(providerId);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setRole("ROLE_USER");

            // CustomOAuth2User 객체 반환
            return new CustomOAuth2User(userDTO);
        } else {
            // 이미 DB에 저장되어 있는 경우 데이터 업데이트
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            // 변경된 사용자 정보 DB에 저장
            userRepository.save(existData);

            // 기존 사용자 정보를 UserDTO로 변환하여 반환
            UserDTO userDTO = new UserDTO();
            userDTO.setProviderId(existData.getProviderId());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            // CustomOAuth2User 객체 반환
            return new CustomOAuth2User(userDTO);
        }
    }
}
