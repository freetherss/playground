package kr.co.inwoo6325.WebGame.service;

import kr.co.inwoo6325.WebGame.model.entity.UserAccount;
import kr.co.inwoo6325.WebGame.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 사용자 정보를 조회합니다.
        UserAccount userAccount = userAccountRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자 " + username + "을(를) 찾을 수 없습니다."));

        // Spring Security의 User 객체로 변환하여 반환합니다.
        return new User(
            userAccount.getUsername(),
            userAccount.getPassword(),
            // 현재는 모든 사용자에게 "ROLE_USER" 권한을 부여합니다.
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")) 
        );
    }
}