package co.fullstacklabs.battlemonsters.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BattleDTO {
    
    private Integer id;
    private MonsterDTO monsterA;
    private MonsterDTO monsterB;
    private MonsterDTO winner;
}
