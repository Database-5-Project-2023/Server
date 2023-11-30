package com.spring.databasebike.domain.station.repository;

import com.spring.databasebike.domain.bike.entity.Bike;
import com.spring.databasebike.domain.station.entity.*;

import java.util.List;
import java.util.Optional;

public interface StationRepository {

    // Station save(Station station);

    void save(CreateStationReq createStationForm);
    void borrowGeneralBike(BorrowGeneralBikeReq bikeReq, String memberId);
    void returnGeneralBike(ReturnGeneralBikeReq bikeReq, String memberId);

    List<Bike> getBikeListByStationId(String stationId);

    List<Station> getStationByLoanCount();

    Optional<Station> findById(String stationId);
    Station findByStationId(String stationId);
    Optional<Station> findByBorough(String borough);
    List<Station> findByAddress(String address);
    List<Station> findAll();

    StationLocation findLocationByStationId(String stationId);
    List<StationLocation> getStationListByLocation();

    void delete(String stationId);
}
