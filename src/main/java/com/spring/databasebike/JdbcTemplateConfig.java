package com.spring.databasebike;

import com.spring.databasebike.domain.bike.repository.BikeRepository;
import com.spring.databasebike.domain.bike.repository.JdbcBikeRepository;
import com.spring.databasebike.domain.station.repository.JdbcStationRepository;
import com.spring.databasebike.domain.station.repository.StationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {

    private final DataSource dataSource;

    public JdbcTemplateConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }

//    @Bean
//    public StationService stationService() {
//        return new StationService(stationRepository());
//    }

    @Bean
    public StationRepository stationRepository() {
        return new JdbcStationRepository(dataSource);
    }

    @Bean
    public BikeRepository bikeRepository() {
        return new JdbcBikeRepository(dataSource);
    }
}
