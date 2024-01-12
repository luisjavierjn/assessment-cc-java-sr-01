package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
public class MonsterControllerTest {
    private static final String MONSTER_PATH = "/monster";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFetchAllMonsters() throws Exception {
        this.mockMvc.perform(get(MONSTER_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].name", Is.is("Dead Unicorn")))
                .andExpect(jsonPath("$[0].attack", Is.is(50)))
                .andExpect(jsonPath("$[0].defense", Is.is(40)))
                .andExpect(jsonPath("$[0].hp", Is.is(30)))
                .andExpect(jsonPath("$[0].speed", Is.is(25)));

    }

    @Test
    void shouldGetMosterSuccessfully() throws Exception {
        long id = 2l;
        this.mockMvc.perform(get(MONSTER_PATH + "/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is("Old Shark")));
    }

    @Test
    void shoulGetMonsterNotExists() throws Exception {
        long id = 3000l;
        this.mockMvc.perform(get(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void shouldCreateAndDeleteMonsterSuccessfully() throws Exception {

        MonsterDTO newMonster = MonsterDTO.builder().name("Monster 4")
                .attack(50).defense(30).hp(30).speed(22)
                .imageUrl("ImageURL1").build();

        MvcResult result = this.mockMvc.perform(post(MONSTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMonster)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        MonsterDTO createdMonster = objectMapper.readValue(responseContent, MonsterDTO.class);
        int createdId = createdMonster.getId();

        this.mockMvc.perform(delete(MONSTER_PATH + "/{id}", createdId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMonsterNotFound() throws Exception {
        int id = 5000;

        this.mockMvc.perform(delete(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isNotFound());
    }
    
     @Test
     void testImportCsvSucessfully() throws Exception {
         String filePath = "data/monsters-correct.csv";
         File file = new File(filePath);

         if (!file.exists()) {
             throw new IllegalArgumentException("CSV file not found: " + filePath);
         }

         InputStream inputStream = Files.newInputStream(file.toPath());
         MockMultipartFile multipartFile = new MockMultipartFile("file", "monsters-correct.csv", "text/csv", inputStream);
         this.mockMvc.perform(MockMvcRequestBuilders.multipart(MONSTER_PATH + "/import").file(multipartFile))
                 .andExpect(status().isOk());
     }
     
     @Test
     void testImportCsvInexistenctColumns() throws Exception {
         String filePath = "data/monsters-wrong-column.csv";
         File file = new File(filePath);

         if (!file.exists()) {
             throw new IllegalArgumentException("CSV file not found: " + filePath);
         }

         InputStream inputStream = Files.newInputStream(file.toPath());
         MockMultipartFile multipartFile = new MockMultipartFile("file", "monsters-wrong-column.csv", "text/csv", inputStream);

         assertThrows(NestedServletException.class, () ->
                 this.mockMvc.perform(MockMvcRequestBuilders.multipart(MONSTER_PATH + "/import").file(multipartFile))
                 .andExpect(status().isOk()));
     }
     

}
