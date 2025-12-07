package kr.co.inwoo6325.WebGame.controller;

import kr.co.inwoo6325.WebGame.model.dto.CharacterCreateRequest;
import kr.co.inwoo6325.WebGame.model.entity.Character;
import kr.co.inwoo6325.WebGame.service.ICharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final ICharacterService characterService; // DIP

    @PostMapping // 또는 @PostMapping("/characters")
    public ResponseEntity<Character> createCharacter(
        @RequestBody CharacterCreateRequest request,
        // 🚨 @AuthenticationPrincipal 대신 Principal 객체 주입을 시도합니다.
        java.security.Principal principal) { 
        
        try {
            // Principal 객체에서 사용자 ID (username)를 추출합니다.
            String currentUsername = principal.getName(); 
            
            Character createdCharacter = characterService.createCharacter(currentUsername, request);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCharacter);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // [SR004] 캐릭터 목록 조회
    @GetMapping
    public ResponseEntity<List<Character>> getCharacterList(java.security.Principal principal) {
        // will/ JWT 토큰에서 userId 추출 필요
        String currentUsername = principal.getName();
        List<Character> characters = characterService.getCharacterList(currentUsername);
        return new ResponseEntity<>(characters, HttpStatus.OK);
    }
    
    // will: getCharacterInfo, selectCharacter 메서드는 추후 추가 (KISS/YAGNI)
}