package co.fullstacklabs.battlemonsters.challenge.service;

import java.io.InputStream;
import java.util.List;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public interface MonsterService {
    
    MonsterDTO create(MonsterDTO monsterDTO);

    MonsterDTO findById(int id);

    MonsterDTO update(MonsterDTO monsterDTO);

    void delete(Integer id);

    void importFromInputStream(InputStream inputStream);

    List<MonsterDTO> getAll();
}
