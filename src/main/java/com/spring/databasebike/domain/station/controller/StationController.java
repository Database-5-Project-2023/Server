package com.spring.databasebike.domain.station.controller;

import com.spring.databasebike.domain.station.entity.CreateStationReq;
import com.spring.databasebike.domain.station.entity.GetStationRes;
import com.spring.databasebike.domain.station.entity.Station;
import com.spring.databasebike.domain.station.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class StationController {

    private final StationService stationService;

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }


    /**
     * 관리자: 대여소 추가
     * @param createStationReq
     * @return
     */
    @PostMapping("/admin/station/add")
    public String createStation(@RequestBody CreateStationReq createStationReq) {
//        Station station = new Station();
//        station.setStation_id(stationForm.getStation_id());
//        station.setStation_addr1(stationForm.getStation_addr1());
//        station.setStation_addr2(stationForm.getStation_addr2());
//        station.setBorough(stationForm.getBorough());
//        station.setHolder_num(stationForm.getHolder_num());

        stationService.createStation(createStationReq);

        return createStationReq.getStation_addr1();
    }

    /**
     * 관리자: 대여소 목록 조회
     * @return
     */
    @GetMapping("/admin/station")
    public List<Station> getStationList() {

        return stationService.findAllStations();

    }

    /**
     * 관리자, 이용자: 대여소 하나의 정보 조회
     * @param stationId
     * @return
     */
    @GetMapping("/station")
    public GetStationRes getStation(@RequestParam("stationId") int stationId) {

//        Station station = stationService.findByStationId(String.valueOf(stationId));
//
//        GetStationRes getStationRes = new GetStationRes();
//        getStationRes.setId(station.getStation_id());
//        getStationRes.setStation_addr2(station.getStation_addr2());
//        getStationRes.setGeneral_bike(station.getGeneral_bike());
//        getStationRes.setSprout_bike(station.getSprout_bike());
//        getStationRes.setRemainder_holder(station.getHolder_num() - station.getGeneral_bike() - station.getSprout_bike());
//
//        return getStationRes;

        return stationService.findByStationId(String.valueOf(stationId));
    }

    @DeleteMapping("/admin/station/delete/{stationId}")
    public String deleteStation(@PathVariable int stationId) {

        String deletedStationId = stationService.findByStationId(String.valueOf(stationId)).getId();
        stationService.deleteStation(String.valueOf(stationId));

        return deletedStationId;
    }


}
