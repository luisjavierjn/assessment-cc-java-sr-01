package co.fullstacklabs.battlemonsters.challenge.service;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;

import java.util.List;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public interface BattleService {
    
    List<BattleDTO> getAll();

    BattleDTO create(BattleDTO battleDTO);

    void delete(Integer id);
}
