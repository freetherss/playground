package kr.co.inwoo6325.WebGame.config;

// ... (필요한 import는 그대로 유지)

import kr.co.inwoo6325.WebGame.filter.JwtAuthenticationFilter;
// import kr.co.inwoo6325.WebGame.service.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // private final JwtTokenProvider jwtTokenProvider;

    // public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    //     this.jwtTokenProvider = jwtTokenProvider;
    // }

    @Bean
public SecurityFilterChain securityFilterChain(
        HttpSecurity http, 
        // 🚨 1. JwtAuthenticationFilter가 파라미터로 잘 주입되는지
        JwtAuthenticationFilter jwtAuthenticationFilter 
    ) throws Exception {
    
    http
        .csrf(AbstractHttpConfigurer::disable)
        
        // 🚨 2. 세션 관리가 STATELESS인지
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        
        // 🚨 3. 경로 허용 (403의 가장 큰 원인)
        .authorizeHttpRequests(auth -> auth
            // HTML 파일 자체와 인증/인가 API는 모두 허용
            .requestMatchers("/api/auth/**", "/register.html", "/login.html", "/success.html").permitAll()
            // 정적 리소스 경로도 허용
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .anyRequest().authenticated() 
        )
        
        // 🚨 4. 필터 등록 확인
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    // ... (PasswordEncoder, AuthenticationManager Bean 설정은 그대로 유지)
    
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}