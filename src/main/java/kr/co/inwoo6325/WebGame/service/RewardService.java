package kr.co.inwoo6325.WebGame.service;

import kr.co.inwoo6325.WebGame.model.entity.Character;
import kr.co.inwoo6325.WebGame.model.entity.Monster;
import kr.co.inwoo6325.WebGame.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardService implements IRewardService {

    private final CharacterRepository characterRepository;

    @Override
    public String giveRewards(Character character, Monster defeatedMonster) {
        // Give EXP
        character.setExp(character.getExp() + defeatedMonster.getExpDrop());
        // TODO: Implement level-up logic here later

        // Give Gold
        character.setGold(character.getGold() + defeatedMonster.getGoldDrop());

        characterRepository.save(character); // Save updated character state

        return String.format("%s가(이) %s를 처치하고 경험치 %d, 골드 %d를 획득했습니다.",
                character.getName(), defeatedMonster.getName(), defeatedMonster.getExpDrop(), defeatedMonster.getGoldDrop());
    }
}
