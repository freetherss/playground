package kr.co.inwoo6325.WebGame.controller;

import kr.co.inwoo6325.WebGame.model.dto.BattleSession;
import kr.co.inwoo6325.WebGame.model.dto.BattleResponse;
import kr.co.inwoo6325.WebGame.model.entity.Character;
import kr.co.inwoo6325.WebGame.model.entity.Combatant;
import kr.co.inwoo6325.WebGame.model.entity.Monster;
import kr.co.inwoo6325.WebGame.repository.BattleRepository;
import kr.co.inwoo6325.WebGame.repository.CharacterRepository;
import kr.co.inwoo6325.WebGame.repository.MonsterRepository;
import kr.co.inwoo6325.WebGame.service.IBattleService;
import kr.co.inwoo6325.WebGame.service.IRewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import kr.co.inwoo6325.WebGame.model.entity.UserAccount;
import kr.co.inwoo6325.WebGame.repository.UserAccountRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/battles")
@RequiredArgsConstructor
@Slf4j
public class BattleController {

    private final IBattleService battleService;
    private final BattleRepository battleRepository;
    private final CharacterRepository characterRepository;
    private final MonsterRepository monsterRepository;
    private final IRewardService rewardService;
    private final UserAccountRepository userAccountRepository;

    @PostMapping("/start")
    public ResponseEntity<BattleSession> startBattle(@RequestParam("characterId") Long characterId, @RequestParam("monsterId") Long monsterId) {
        log.info("Attempting to start battle with characterId: {} and monsterId: ", characterId, monsterId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        Character playerCharacter = characterRepository.findById(characterId)
                .orElseThrow(() -> {
                    log.error("Character not found with ID: {}", characterId);
                    return new IllegalArgumentException("Character not found with ID: " + characterId);
                });
        
        if (!playerCharacter.getUser().equals(user)) {
            log.error("Character with ID {} does not belong to user {}", characterId, username);
            return ResponseEntity.status(403).build();
        }

        log.info("Found character: {}", playerCharacter.getName());

        Monster enemyMonster = monsterRepository.findById(monsterId)
               .orElseThrow(() -> {
                   log.error("Monster not found with ID: {}", monsterId);
                   return new IllegalArgumentException("Monster not found with ID: " + monsterId);
               });
        log.info("Found monster: {}", enemyMonster.getName());

        List<Combatant> combatants = new ArrayList<>();
        combatants.add(playerCharacter);
        combatants.add(enemyMonster);

        List<Combatant> turnOrder = battleService.determineTurnOrder(combatants);

        BattleSession session = new BattleSession(combatants, turnOrder);
        battleRepository.save(session);
        log.info("Battle session created and saved with ID: {}", session.getBattleId());

        return ResponseEntity.ok(session);
    }
    
    @PostMapping("/{battleId}/attack")
    public ResponseEntity<BattleResponse> performAttack(@PathVariable("battleId") String battleId) { // Changed return type
        log.info("Attempting to perform attack for battleId: {}", battleId);

        BattleSession session = battleRepository.findById(battleId)
                .orElseThrow(() -> new IllegalArgumentException("Battle session not found: " + battleId));
        log.info("Found battle session: {}", session.getBattleId());

        if (session.isBattleOver()) {
            log.warn("Battle {} is already over.", battleId);
            return ResponseEntity.badRequest().body(BattleResponse.builder()
                    .battleLog("Battle is already over.")
                    .battleSession(session)
                    .build());
        }

        Combatant currentAttacker = session.getCurrentAttacker();
        if (currentAttacker == null) {
            log.error("No current attacker in this turn for battleId: {}", battleId);
            return ResponseEntity.badRequest().body(BattleResponse.builder()
                    .battleLog("No current attacker in this turn.")
                    .battleSession(session)
                    .build());
        }
        log.info("Current attacker: {}", currentAttacker.getName());

        Combatant defender = session.getCombatants().stream()
                .filter(c -> !c.equals(currentAttacker))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Defender not found in battle."));
        log.info("Defender: {}", defender.getName());

        String battleLog = battleService.performAttack(currentAttacker, defender);
        String rewardMessage = null;

        // Check if defender is defeated
        if (defender.getCurrentHp() <= 0) {
            session.setBattleOver(true);
            battleLog += "\n" + defender.getName() + "이(가) 쓰러졌습니다! 전투 종료.";
            log.info("Battle {} is over. Defender {} defeated.", battleId, defender.getName());

            // If the defender is a Monster and the attacker is a Character, give rewards
            if (defender instanceof Monster && currentAttacker instanceof Character) {
                rewardMessage = rewardService.giveRewards((Character) currentAttacker, (Monster) defender);
                battleLog += "\n" + rewardMessage;
            }
        } else {
            session.nextTurn(); // Advance turn only if battle is not over
            log.info("Turn advanced for battleId: {}. New attacker: {}", battleId, session.getCurrentAttacker().getName());
        }

        battleRepository.save(session); // Update the session in the repository
        log.info("Battle session {} updated.", battleId);

        return ResponseEntity.ok(BattleResponse.builder()
                .battleLog(battleLog)
                .battleSession(session)
                .rewardMessage(rewardMessage)
                .build());
    }
}