package kr.co.inwoo6325.WebGame.service;

import kr.co.inwoo6325.WebGame.model.dto.CharacterCreateRequest;
import kr.co.inwoo6325.WebGame.model.entity.Character;

import java.util.List;

public interface ICharacterService {
    
    // 🚨 String username 기반으로 통일
    Character createCharacter(String username, CharacterCreateRequest request); 
    
    // 🚨 컴파일 오류 해결을 위해 String username 기반으로 통일
    List<Character> getCharacterList(String username);
}