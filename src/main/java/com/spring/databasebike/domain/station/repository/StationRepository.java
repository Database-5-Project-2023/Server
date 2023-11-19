package com.spring.databasebike.domain.station.repository;

import com.spring.databasebike.domain.station.entity.Station;
import com.spring.databasebike.domain.station.entity.CreateStationReq;

import java.util.List;
import java.util.Optional;

public interface StationRepository {

    // Station save(Station station);

    void save(CreateStationReq createStationForm);

    Optional<Station> findById(String stationId);
    Station findByStationId(String stationId);
    Optional<Station> findByBorough(String borough);
    List<Station> findAll();

    void delete(String stationId);
}
