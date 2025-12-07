package kr.co.inwoo6325.WebGame.service;

import kr.co.inwoo6325.WebGame.model.dto.UserRegisterRequest;
import kr.co.inwoo6325.WebGame.model.entity.UserAccount;
import kr.co.inwoo6325.WebGame.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // LocalDateTime 임포트

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     * 회원가입 처리
     */
    @Override
    @Transactional
    public void register(UserRegisterRequest request) {
        // 🚨 수정 1: findByUsername()이 반환하는 Optional<UserAccount>를 
        // Optional.isPresent()를 사용하여 정확하게 확인합니다.
        if (userAccountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        
        // 새로운 UserAccount 객체 생성
        UserAccount newUser = new UserAccount();
        newUser.setUsername(request.getUsername());
        
        // 비밀번호 암호화 저장
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        
        // 🚨 수정 2: UserAccount 엔티티에 필수(Not Null) 필드인 createdAt을 설정합니다.
        // UserAccount 엔티티에 setCreatedAt() 메서드가 정의되어 있어야 합니다.
        newUser.setCreatedAt(LocalDateTime.now()); 
        
        userAccountRepository.save(newUser);
    }

    /**
     * 로그인 및 JWT 토큰 발급 처리 (AuthenticationManager 우회 및 직접 인증)
     */
    @Override
    public String login(String username, String password) {
        
        // 1. UserDetailsService를 통해 DB에서 사용자 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 2. PasswordEncoder를 사용하여 비밀번호 직접 검증 (핵심 로직)
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            // 비밀번호가 일치하지 않으면 예외 발생
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        
        // 3. 검증 성공 시, UserDetails를 사용하여 JWT 토큰 생성 및 반환
        return jwtTokenProvider.generateToken(userDetails);
    }
}