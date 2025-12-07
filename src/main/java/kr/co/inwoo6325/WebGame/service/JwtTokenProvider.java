package kr.co.inwoo6325.WebGame.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
// import jakarta.annotation.PostConstruct;
// import kr.co.inwoo6325.WebGame.model.entity.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j // 로그 사용을 위해 추가
@Component
public class JwtTokenProvider {

    // 🚨 HS512 알고리즘 요구 사항: 512비트(64자) 이상의 안전한 키 문자열
    // 이 문자열을 사용하여 512비트 이상의 키를 생성합니다.
    private final String secretKey = "a_very_long_and_secure_secret_key_for_web_game_jwt_hs512_signing_algorithm_required_512bits_for_security_compliance"; 

    private Key key;
    // 토큰 유효 시간: 1시간 (밀리초)
    private final long accessTokenValidityInMilliseconds = 3600000; 

    private final UserDetailsService userDetailsService;
    
    // UserDetailsService를 주입받아 인증 과정에서 사용합니다.
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        // 생성자에서 secretKey를 Key 객체로 변환합니다.
        try {
            byte[] keyBytes = secretKey.getBytes("UTF-8");
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (UnsupportedEncodingException e) {
            log.error("JWT Secret Key encoding failed: {}", e.getMessage());
            // 시스템을 멈추거나, 안전한 기본 키로 대체하는 등의 예외 처리 필요
            throw new RuntimeException("JWT Key initialization error", e);
        }
    }

    /**
     * JWT 토큰 생성 (UserDetails 기반)
     */
    public String generateToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        
        // 권한 정보 추가 (현재는 Roles를 사용하지 않으므로, 더미 데이터 혹은 생략 가능)
        String authorities = userDetails.getAuthorities().stream()
            .map(auth -> auth.getAuthority()) // .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        claims.put("auth", authorities);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims) // 데이터
            .setIssuedAt(now) // 토큰 발행일자
            .setExpiration(validity) // 만료일시
            .signWith(key, SignatureAlgorithm.HS512) 
            .compact();
    }
    
    /**
     * JWT 토큰 생성 (Authentication 기반) - 권장
     */
    public String generateToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        
        String authorities = authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority()) // .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        claims.put("auth", authorities);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims) // 데이터
            .setIssuedAt(now) // 토큰 발행일자
            .setExpiration(validity) // 만료일시
            .signWith(key, SignatureAlgorithm.HS512) 
            .compact();
    }

    /**
     * JWT 토큰에서 인증 정보 조회
     * SecurityContextHolder에 저장할 Authentication 객체를 생성합니다.
     */
    public Authentication getAuthentication(String token) {
        // 토큰에서 사용자 이름을 추출하여 UserDetails 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        // 인증 객체 반환
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    /**
     * 토큰에서 회원 이름(Username) 추출
     */
    public String getUsername(String token) {
        // 🚨 레거시 parser 방식 사용
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    /**
     * 토큰의 유효성 + 만료일자 확인
     */
    public boolean validateToken(String token) {
        try {
            // 🚨 레거시 parser 방식 사용
            Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            
            // 만료일자가 지나지 않았으면 true 반환
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}