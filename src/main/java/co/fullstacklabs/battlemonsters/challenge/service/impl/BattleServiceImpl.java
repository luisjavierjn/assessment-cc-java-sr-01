package co.fullstacklabs.battlemonsters.challenge.service.impl;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;
import co.fullstacklabs.battlemonsters.challenge.service.game.BattleStateMachine;
import co.fullstacklabs.battlemonsters.challenge.service.game.Event;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Service
public class BattleServiceImpl implements BattleService {

    private BattleRepository battleRepository;
    private ModelMapper modelMapper;
    private BattleStateMachine battleSM;
    private MonsterRepository monsterRepository;

   
    @Autowired
    public BattleServiceImpl(BattleRepository battleRepository,
                             ModelMapper modelMapper,
                             BattleStateMachine battleSM,
                             MonsterRepository monsterRepository) {
        this.battleRepository = battleRepository;
        this.modelMapper = modelMapper;
        this.battleSM = battleSM;
        this.monsterRepository = monsterRepository;
    }

    /**
     * List all existence battles
     */
    @Override
    public List<BattleDTO> getAll() {
        List<Battle> battles = battleRepository.findAll();
        return battles.stream().map(battle -> modelMapper.map(battle, BattleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BattleDTO create(BattleDTO battleDTO) {
        monsterRepository.findById(battleDTO.getMonsterA().getId())
                .orElseThrow(() -> new ResourceNotFoundException("MonsterA not found"));

        monsterRepository.findById(battleDTO.getMonsterB().getId())
                .orElseThrow(() -> new ResourceNotFoundException("MonsterB not found"));

        Queue<Event> queue = battleSM.getQueue();
        queue.add(Event.GAME_EVT_START);
        while(queue.size() > 0) {
            Event evt = queue.remove();
            battleSM.check(evt, battleDTO);
        }

        Battle battle = modelMapper.map(battleDTO, Battle.class);
        battle = battleRepository.save(battle);
        return modelMapper.map(battle, BattleDTO.class);
    }

    private Battle findBattleById(int id) {
        return battleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Battle not found"));
    }

    @Override
    public void delete(Integer id) {
        Battle battle = findBattleById(id);
        battleRepository.delete(battle);
    }

}
