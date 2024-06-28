package org.example.shallweeatbackend.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * JwtSecretKeyGenerator 클래스는 JWT(JSON Web Token)를 위한 비밀 키를 생성하는 유틸리티 클래스입니다.
 * 이 클래스는 임의의 바이트 배열을 생성하고 이를 Base64 인코딩하여 비밀 키로 반환합니다.
 */
public class JwtSecretKeyGenerator {

    /**
     * 주어진 길이의 비밀 키를 생성하는 메서드입니다.
     *
     * @param length 생성할 비밀 키의 길이 (바이트 단위)
     * @return Base64 인코딩된 비밀 키 문자열
     */
    public static String generateSecretKey(int length) {
        SecureRandom secureRandom = new SecureRandom(); // 보안 난수 생성기 초기화
        byte[] key = new byte[length]; // 지정된 길이의 바이트 배열 생성
        secureRandom.nextBytes(key); // 바이트 배열에 임의의 값 채우기
        return Base64.getEncoder().encodeToString(key); // 바이트 배열을 Base64로 인코딩하여 문자열로 반환
    }

    /**
     * 메인 메서드는 이 클래스의 기능을 테스트하기 위해 사용됩니다.
     * 256비트 (32 바이트) 길이의 비밀 키를 생성하고 출력합니다.
     *
     * @param args 실행 시 인자로 전달되는 값 (사용되지 않음)
     */
    public static void main(String[] args) {
        String secretKey = generateSecretKey(32); // 256비트 키 생성 (32 바이트)
        System.out.println("Generated Secret Key: " + secretKey); // 생성된 비밀 키 출력
    }
}