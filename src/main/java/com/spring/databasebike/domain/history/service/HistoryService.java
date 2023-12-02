package com.spring.databasebike.domain.history.service;

import com.spring.databasebike.domain.history.entity.GetHistoryRes;
import com.spring.databasebike.domain.history.repository.HistoryRepository;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.member.repository.MemberRepository;
import com.spring.databasebike.domain.station.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    private final StationRepository stationRepository;

    private final MemberRepository memberRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository, StationRepository stationRepository, MemberRepository memberRepository){
        this.historyRepository = historyRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
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

    //특정 회원의 대여/반납 이력 조회
    public List<GetHistoryRes> memHistoryList(String id, int start, int end){
        List<History> result = historyRepository.getHistoryList(id, start, end);

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

    //특정 회원의 대여/반납 이력 개수
    public Integer memHistoryNum(String id){
        return historyRepository.getTotalHistory(id);
    }

    //특정 회원의 특정 기간동안의 대여/반납 이력 조회
    public List<GetHistoryRes> memSearchHistoryList(String id, String period, String start_date, String end_date, int start, int end){
        List<History> result = historyRepository.getSearchHistoryList(id, period, start_date, end_date, start, end);

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
