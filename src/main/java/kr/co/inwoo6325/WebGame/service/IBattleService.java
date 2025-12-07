package kr.co.inwoo6325.WebGame.service;

import kr.co.inwoo6325.WebGame.model.entity.Combatant;
import java.util.List;

public interface IBattleService {

    /**
     * 한 전투 단위(턴)를 수행합니다. 공격자가 방어자를 공격하고, 그 결과를 반환합니다.
     * @param attacker 공격하는 Combatant
     * @param defender 방어하는 Combatant
     * @return 전투 결과 로그
     */
    String performAttack(Combatant attacker, Combatant defender);

    /**
     * 전투 참여자 리스트를 받아 민첩성(agility)을 기준으로 내림차순 정렬하여 턴 순서를 결정합니다.
     * @param combatants 전투에 참여하는 모든 Combatant 리스트
     * @return 민첩성 순으로 정렬된 Combatant 리스트
     */
    List<Combatant> determineTurnOrder(List<Combatant> combatants);

}
