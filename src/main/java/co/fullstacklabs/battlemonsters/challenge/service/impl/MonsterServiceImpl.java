package co.fullstacklabs.battlemonsters.challenge.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Service
public class MonsterServiceImpl implements MonsterService {

    private MonsterRepository monsterRepository;
    private ModelMapper modelMapper;

    public MonsterServiceImpl(MonsterRepository monsterRepository, ModelMapper modelMapper) {
        this.monsterRepository = monsterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MonsterDTO create(MonsterDTO monsterDTO) {
        Monster monster = modelMapper.map(monsterDTO, Monster.class);
        monster = monsterRepository.save(monster);
        return modelMapper.map(monster, MonsterDTO.class);
    }

    private Monster findMonsterById(int id) {
         return monsterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monster not found"));
    }

    @Override
    public MonsterDTO findById(int id) {
        Monster monster = findMonsterById(id);
        return modelMapper.map(monster, MonsterDTO.class);
    }

    @Override
    public MonsterDTO update(MonsterDTO monsterDTO) {
        Monster monster = modelMapper.map(monsterDTO, Monster.class);
        monsterRepository.save(monster);
        return modelMapper.map(monster, MonsterDTO.class);

    }

    @Override
    public void delete(Integer id) {
        Monster monster = findMonsterById(id);
        monsterRepository.delete(monster);
    }

    @Override
    public void importFromInputStream(InputStream inputStream) {
        try (Reader inputReader = new InputStreamReader(inputStream, "UTF-8")) {
            BeanListProcessor<MonsterDTO> rowProcessor = new BeanListProcessor<>(MonsterDTO.class);
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);
            settings.setProcessor(rowProcessor);
            CsvParser parser = new CsvParser(settings);
            parser.parse(inputReader);
            List<MonsterDTO> monsters = rowProcessor.getBeans();            
            monsters.forEach(m -> create(m));
        } catch (IOException ex) {
            throw new UnprocessableFileException(ex.getMessage());
        }
    }

    @Override
    public List<MonsterDTO> getAll() {
        List<Monster> monsters = monsterRepository.findAll();

        // Mapear la lista de Monster a una lista de MonsterDTO usando ModelMapper y Stream API
        return monsters.stream()
                .map(monster -> modelMapper.map(monster, MonsterDTO.class))
                .collect(Collectors.toList());
    }

}
