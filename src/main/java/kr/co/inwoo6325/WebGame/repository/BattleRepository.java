package kr.co.inwoo6325.WebGame.repository;

import kr.co.inwoo6325.WebGame.model.dto.BattleSession;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BattleRepository {
    private final Map<String, BattleSession> sessions = new ConcurrentHashMap<>();

    public void save(BattleSession session) {
        sessions.put(session.getBattleId(), session);
    }

    public Optional<BattleSession> findById(String battleId) {
        return Optional.ofNullable(sessions.get(battleId));
    }

    public void delete(String battleId) {
        sessions.remove(battleId);
    }
}
