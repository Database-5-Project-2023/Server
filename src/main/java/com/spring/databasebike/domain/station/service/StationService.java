package com.spring.databasebike.domain.station.service;

import com.spring.databasebike.domain.station.entity.GetStationRes;
import com.spring.databasebike.domain.station.entity.Station;
import com.spring.databasebike.domain.station.entity.CreateStationReq;
import com.spring.databasebike.domain.station.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void createStation(CreateStationReq createStationForm) {

        validateDuplicateStation(createStationForm);
        stationRepository.save(createStationForm);
        // return createStationForm.getStation_addr1();
    }

    private void validateDuplicateStation(CreateStationReq createStationReq) {
        stationRepository.findById(createStationReq.getStation_id())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 대여소 아이디입니다!!!");
                });
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    public Optional<Station> findOneStation(String stationId) {
        return stationRepository.findById(stationId);
    }

    public GetStationRes findByStationId(String id) {

        checkStationExist(id);

        Station station = stationRepository.findByStationId(id);

        GetStationRes getStationRes = new GetStationRes();
        getStationRes.setId(station.getStation_id());
        getStationRes.setStation_addr2(station.getStation_addr2());
        getStationRes.setGeneral_bike(station.getGeneral_bike());
        getStationRes.setSprout_bike(station.getSprout_bike());
        getStationRes.setRemainder_holder(station.getHolder_num() - station.getGeneral_bike() - station.getSprout_bike());

        // return stationRepository.findByStationId(id);
        return getStationRes;
    }

    public void deleteStation(String stationId) {
        stationRepository.delete(stationId);
    }

    private void checkStationExist(String stationId) {
        if(stationRepository.findById(stationId).isEmpty()){
            throw new IllegalStateException("존재하지 않는 대여소 정보입니다!!!");
        }
    }

}
