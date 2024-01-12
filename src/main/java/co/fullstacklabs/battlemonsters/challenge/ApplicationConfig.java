package co.fullstacklabs.battlemonsters.challenge;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}