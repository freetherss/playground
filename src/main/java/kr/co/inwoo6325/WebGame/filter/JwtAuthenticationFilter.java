package kr.co.inwoo6325.WebGame.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.inwoo6325.WebGame.service.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component; // 🚨 누락된 @Component 임포트
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 🚨 @Component 어노테이션을 추가하여 Spring Bean으로 등록합니다.
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        log.debug("Extracted JWT token from request: {}", token != null ? "Present" : "Absent");


        if (token != null) {
            boolean isValidToken = jwtTokenProvider.validateToken(token);
            log.debug("JWT token validation result by JwtAuthenticationFilter: {}", isValidToken);

            if (isValidToken) {
                // 토큰에서 사용자 이름 추출
                String username = jwtTokenProvider.getUsername(token);
                log.debug("Username extracted from token: {}", username);

                // UserDetailsService를 통해 UserDetails 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.debug("UserDetails loaded for user: {}", userDetails.getUsername());

                // 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // SecurityContext에 인증 정보 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authentication set in SecurityContext for user: {}", userDetails.getUsername());
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출하는 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}