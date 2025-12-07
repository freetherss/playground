package kr.co.inwoo6325.WebGame.repository;

import kr.co.inwoo6325.WebGame.model.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository 또는 CrudRepository를 상속받아야 합니다.
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    
    // 🚨 JPA Query Method 추가: username(String)으로 UserAccount 조회
    Optional<UserAccount> findByUsername(String username); 
}