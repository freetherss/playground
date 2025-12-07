package kr.co.inwoo6325.WebGame.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BattleResponse {
    private String battleLog;
    private BattleSession battleSession;
    private String rewardMessage;
}
