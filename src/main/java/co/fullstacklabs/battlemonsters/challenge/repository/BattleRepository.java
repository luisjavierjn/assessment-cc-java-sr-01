package co.fullstacklabs.battlemonsters.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.fullstacklabs.battlemonsters.challenge.model.Battle;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public interface BattleRepository extends JpaRepository<Battle, Integer> {
    
}
