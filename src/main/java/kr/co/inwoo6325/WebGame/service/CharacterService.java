package kr.co.inwoo6325.WebGame.service;

import java.util.List;
import kr.co.inwoo6325.WebGame.model.dto.CharacterCreateRequest;
import kr.co.inwoo6325.WebGame.model.entity.Character;
import kr.co.inwoo6325.WebGame.model.entity.UserAccount;
import kr.co.inwoo6325.WebGame.repository.CharacterRepository;
import kr.co.inwoo6325.WebGame.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterService implements ICharacterService {

    private final UserAccountRepository userAccountRepository;
    private final CharacterRepository characterRepository;

    @Override
    @Transactional
    public Character createCharacter(String username, CharacterCreateRequest request) {
        
        // 0. 유효성 검사는 Controller에서 @Valid를 통해 처리되므로 서비스 레이어에서는 제거합니다.
        // 1. String username으로 UserAccount 조회
        UserAccount user = getUserByUsername(username);
        
        // 2. 캐릭터 이름 중복 체크 (CharacterRepository에 existsByName(String name) 필요)
        if (characterRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 캐릭터 이름입니다.");
        }

        // 3. Character 엔티티 생성
        Character newCharacter = Character.builder()
                .name(request.getName())
                .job(request.getJob())
                .user(user) // UserAccount 엔티티 연결
                .build();
        
        // 4. 저장 및 반환
        return characterRepository.save(newCharacter);
    }
    
    // 🚨 ICharacterService의 getCharacterList(String username) 구현
    @Override
    public List<Character> getCharacterList(String username) {
        UserAccount user = getUserByUsername(username);
        return characterRepository.findByUser(user); // 🚨 findByUser(UserAccount user) 호출로 수정
    }

    /**
     * 사용자 이름으로 UserAccount 엔티티를 조회하는 헬퍼 메서드 (DRY 원칙 적용)
     */
    private UserAccount getUserByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
    }
}