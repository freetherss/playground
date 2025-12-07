package kr.co.inwoo6325.WebGame.model.dto;

import kr.co.inwoo6325.WebGame.model.entity.Combatant;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BattleSession {
    private String battleId;
    private List<Combatant> combatants;
    private List<Combatant> turnOrder;
    private int currentTurnIndex;
    private boolean isBattleOver;

    public BattleSession(List<Combatant> combatants, List<Combatant> turnOrder) {
        this.battleId = UUID.randomUUID().toString();
        this.combatants = combatants;
        this.turnOrder = turnOrder;
        this.currentTurnIndex = 0;
        this.isBattleOver = false;
    }

    public Combatant getCurrentAttacker() {
        if (currentTurnIndex < turnOrder.size()) {
            return turnOrder.get(currentTurnIndex);
        }
        return null;
    }

    // Advance to the next turn
    public void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % turnOrder.size();
    }
}
