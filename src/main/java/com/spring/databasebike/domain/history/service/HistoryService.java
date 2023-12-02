package com.spring.databasebike.domain.history.service;

import com.spring.databasebike.domain.history.entity.GetHistoryRes;
import com.spring.databasebike.domain.history.repository.HistoryRepository;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.station.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    private final StationRepository stationRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository, StationRepository stationRepository){
        this.historyRepository = historyRepository;
        this.stationRepository = stationRepository;
    }

    public List<GetHistoryRes> findHistoryByUserId(String memberId){
        List<History> result = historyRepository.findHistoryByUserId(memberId);

        List<GetHistoryRes> historyResList = new ArrayList<>();

        for(History history: result){
            GetHistoryRes historyRes = new GetHistoryRes();
            historyRes.setUser_id(history.getUser_id());
            historyRes.setBike_id(history.getBike_id());
            String starting_station_addr = stationRepository.findByStationId(history.getStarting_station_id()).getStation_addr2();
            historyRes.setStarting_station_addr(starting_station_addr);
            String arrival_station_addr = stationRepository.findByStationId(history.getArrival_station_id()).getStation_addr2();
            historyRes.setArrival_station_addr(arrival_station_addr);
            historyRes.setStarting_time(history.getStarting_time());
            historyRes.setArrival_time(history.getArrival_time());
            historyRes.setDistance(history.getDistance());
            historyRes.setReturn_status(history.getReturn_status());
            historyResList.add(historyRes);
        }

        return historyResList;
    }

}
