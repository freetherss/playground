package kr.co.inwoo6325.WebGame.service;

import kr.co.inwoo6325.WebGame.model.entity.Combatant;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BattleService implements IBattleService {

    @Override
    public String performAttack(Combatant attacker, Combatant defender) {
        // SRS에 명시된 데미지 공식: Damage = Attack * (1 - Defense / (Defense + 100))
        // 정수 연산으로 인한 손실을 피하기 위해 double로 계산 후 int로 변환
        double rawDamage = attacker.getAttack() * (1 - (double)defender.getDefense() / (defender.getDefense() + 100));
        int damage = Math.max(1, (int)rawDamage); // 최소 1의 데미지를 보장

        int newHp = defender.getCurrentHp() - damage;
        if (newHp < 0) {
            newHp = 0;
        }
        defender.setCurrentHp(newHp);

        return String.format("%s의 공격! %s에게 %d의 피해를 입혔습니다. (남은 HP: %d)",
                attacker.getName(), defender.getName(), damage, defender.getCurrentHp());
    }

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        return combatants.stream()
                .sorted(Comparator.comparingInt(Combatant::getAgility).reversed())
                .collect(Collectors.toList());
    }
}
