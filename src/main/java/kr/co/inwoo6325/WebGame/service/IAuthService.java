package kr.co.inwoo6325.WebGame.service;

import kr.co.inwoo6325.WebGame.model.dto.UserRegisterRequest;

// DIP 원칙에 따라 인터페이스 먼저 정의
public interface IAuthService {
    // 회원가입 메서드
    void register(UserRegisterRequest request);
    
    // 로그인 메서드 (JWT 토큰 반환 등)
    String login(String username, String password); 
}