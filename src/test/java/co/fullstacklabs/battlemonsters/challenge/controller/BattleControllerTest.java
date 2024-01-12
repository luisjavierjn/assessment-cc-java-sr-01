package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BattleControllerTest {
    private static final String BATTLE_PATH = "/battle";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static BattleDTO createdBattle;

    @Test
    @Order(1)
    void shouldFetchAllBattles() throws Exception {
        this.mockMvc.perform(get(BATTLE_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)));
    }
    
    @Test
    @Order(2)
    void shouldFailBattleWithInexistentMonster() throws Exception {
        BattleDTO newBattle = createBattleDTO();
        Integer inexistentMonsterId = 100;
        newBattle.getMonsterB().setId(inexistentMonsterId);

        this.mockMvc.perform(post(BATTLE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBattle)))
                .andExpect(status().isNotFound());
    }


    @Test
    @Order(3)
    void shouldInsertBattleWithMonsterBWinning() throws Exception {
        BattleDTO newBattle = createBattleDTO();
        MvcResult result = this.mockMvc.perform(post(BATTLE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBattle)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        createdBattle = objectMapper.readValue(responseContent, BattleDTO.class);
        assertEquals(createdBattle.getMonsterB().getId(), createdBattle.getWinner().getId());
    }

    @Test
    @Order(4)
    void shouldDeleteBattleSucessfully() throws Exception {
        int createdId = createdBattle.getId();

        this.mockMvc.perform(delete(BATTLE_PATH + "/{id}", createdId))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void shouldFailDeletingInexistentBattle() throws Exception {
        int inexistentBattleId = 100;
        this.mockMvc.perform(delete(BATTLE_PATH + "/{id}", inexistentBattleId))
                .andExpect(status().isNotFound());
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