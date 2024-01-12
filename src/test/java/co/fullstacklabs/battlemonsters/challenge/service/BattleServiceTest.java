package co.fullstacklabs.battlemonsters.challenge.service;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.game.BattleStateMachine;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleServiceImpl;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {
    
    @InjectMocks
    public BattleServiceImpl battleService;

    @Mock
    public BattleRepository battleRepository;

    @Mock
    public BattleStateMachine battleSM;

    @Mock
    private ModelMapper mapper;

    /**** Delete */
    @Mock
    public MonsterRepository monsterRepository;

    @Test
    @Order(1)
    public void testGetAll() {
        
        Battle battle1 = BattleTestBuilder.builder().id(1).build();
        Battle battle2 = BattleTestBuilder.builder().id(2).build();

        List<Battle> battleList = Lists.newArrayList(battle1, battle2);
        when(battleRepository.findAll()).thenReturn(battleList);
        
        battleService.getAll();

        Mockito.verify(battleRepository).findAll();
        Mockito.verify(mapper).map(battleList.get(0), BattleDTO.class);
        Mockito.verify(mapper).map(battleList.get(1), BattleDTO.class);        
    }
    

    @Test
    @Order(2)
    void shouldInsertBattleWithMonsterBWinning() {
        BattleDTO battleDTO = createBattleDTO();
        when(battleSM.getQueue()).thenReturn(new LinkedList<>());
        doCallRealMethod().when(battleSM).check(any(),any());
        when(monsterRepository.findById(any()))
                .thenReturn(Optional.of(MonsterTestBuilder.builder().build()));
        battleService.create(battleDTO);
        assertEquals(battleDTO.getMonsterB().getId(), battleDTO.getWinner().getId());
    }

    @Test
    @Order(3)
    void shouldDeleteBattleSucessfully() {
        int id = 3;
        Battle battle1 = BattleTestBuilder.builder().id(3).build();
        when(battleRepository.findById(id)).thenReturn(Optional.of(battle1));
        Mockito.doNothing().when(battleRepository).delete(battle1);

        battleService.delete(id);

        Mockito.verify(battleRepository).findById(id);
        Mockito.verify(battleRepository).delete(battle1);
    }

    BattleDTO createBattleDTO() {
        MonsterDTO monster1 = MonsterDTO.builder()
                .id(1)
                .name("Dead Unicorn")
                .attack(50)
                .defense(40)
                .hp(30)
                .speed(25)
                .build();

        MonsterDTO monster2 = MonsterDTO.builder()
                .id(2)
                .name("Old Shark")
                .attack(50)
                .defense(20)
                .hp(80)
                .speed(90)
                .build();

        return BattleDTO.builder()
                .id(2)
                .monsterA(monster1)
                .monsterB(monster2)
                .build();
    }
}
