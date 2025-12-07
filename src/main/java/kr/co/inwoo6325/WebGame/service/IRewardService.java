package kr.co.inwoo6325.WebGame.service;

import kr.co.inwoo6325.WebGame.model.entity.Character;
import kr.co.inwoo6325.WebGame.model.entity.Monster;

public interface IRewardService {
    /**
     * 전투에서 승리한 캐릭터에게 몬스터 처치에 따른 보상을 지급합니다.
     * @param character 보상을 받을 캐릭터
     * @param defeatedMonster 처치된 몬스터
     * @return 보상 지급 결과를 포함하는 문자열 메시지
     */
    String giveRewards(Character character, Monster defeatedMonster);
}
