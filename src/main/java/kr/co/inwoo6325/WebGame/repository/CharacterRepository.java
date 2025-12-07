package kr.co.inwoo6325.WebGame.repository;

import kr.co.inwoo6325.WebGame.model.entity.Character;
import kr.co.inwoo6325.WebGame.model.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    
    // Spring Data JPA는 메서드 이름을 분석하여 쿼리를 자동 생성합니다 (DRY)
    
    // 캐릭터 이름 중복 검사에 사용
    boolean existsByName(String name);
    
    // 사용자 엔티티로 캐릭터 목록 조회 (Service에서 사용)
    List<Character> findByUser(UserAccount user);
}