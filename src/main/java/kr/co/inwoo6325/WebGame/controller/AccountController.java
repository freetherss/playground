package kr.co.inwoo6325.WebGame.controller;

import kr.co.inwoo6325.WebGame.model.dto.UserRegisterRequest;
import kr.co.inwoo6325.WebGame.model.dto.LoginRequest; // 🚨 LoginRequest DTO import 확인
import kr.co.inwoo6325.WebGame.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // 🚨 이 경로가 SecurityConfig.java에서 permitAll() 처리됨
@RequiredArgsConstructor
public class AccountController {

    private final IAuthService authService;

    /**
     * POST /api/auth/register : 회원가입 처리
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRegisterRequest request) {
        try {
            authService.register(request);
            // 201 Created 응답 반환
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            // 이미 존재하는 사용자 등, 잘못된 요청은 400 Bad Request 반환
            // 참고: body(e.getMessage())를 통해 오류 메시지를 클라이언트에 전달할 수도 있습니다.
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * POST /api/auth/login : 로그인 및 JWT 토큰 발급 처리
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) { // 🚨 LoginRequest DTO 사용 확인
        try {
            String token = authService.login(request.getUsername(), request.getPassword());
            // 200 OK와 함께 JWT 토큰을 응답 본문에 반환
            return ResponseEntity.ok("로그인 성공: " + token);
        } catch (IllegalArgumentException e) {
            // 아이디 또는 비밀번호 불일치 시 401 Unauthorized 대신 400 Bad Request를 반환합니다.
            // Spring Security 필터 우회 문제로 인해, AuthService에서 던진 예외는 400으로 처리하는 것이 일반적입니다.
            return ResponseEntity.badRequest().body("로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
        }
    }
}