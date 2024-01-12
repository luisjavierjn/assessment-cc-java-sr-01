package co.fullstacklabs.battlemonsters.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.fullstacklabs.battlemonsters.challenge.model.Monster;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public interface MonsterRepository extends JpaRepository<Monster, Integer>{
    
}
