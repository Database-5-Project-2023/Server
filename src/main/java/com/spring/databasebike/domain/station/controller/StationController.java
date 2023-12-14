package com.spring.databasebike.domain.station.controller;

import com.spring.databasebike.domain.bike.entity.Bike;
import com.spring.databasebike.domain.station.entity.*;
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
     * 대여소 별 따릉이 목록 조회
     * @param stationId
     * @return
     */
    @GetMapping("/station/bike/{stationId}")
    public List<Bike> getBikeList(@PathVariable int stationId) {
        return stationService.getBikeListByStationId(String.valueOf(stationId));
    }

    /**
     * 일반 자전거 대여
     * @param bikeReq
     * @param memberId
     * @return
     */
    // TODO: 대여한 사람, 반납한 사람 아이디 정보 출력
    @PostMapping("/station/borrow/bike")
    public String borrowGeneralBike(@RequestPart("bikeReq") BorrowGeneralBikeReq bikeReq,
                                    @RequestPart("member") String memberId) {

        stationService.borrowGeneralBike(bikeReq, memberId);

        return bikeReq.getStarting_station_id();
    }

    /**
     * 일반 자전거 반납
     * @param bikeReq
     * @param memberId
     * @return
     */
    @PostMapping("/station/return/bike")
    public String returnGeneralBike(@RequestPart("bikeReq") ReturnGeneralBikeReq bikeReq,
                                    @RequestPart("member") String memberId) {

        stationService.returnGeneralBike(bikeReq, memberId);

        return bikeReq.getArrival_station_id();
    }

    @GetMapping("/station/location")
    public List<StationLocation> getStationListByLocation(){
        return stationService.getStationListByLocation();
    }

    /**
     * 두 대여소 간의 거리 계산
     * @param starting_station_id
     * @param arrival_station_id
     * @return
     */
    @GetMapping("/station/calculate/distance")
    public Float calculateDistance(@RequestPart("starting_station") int starting_station_id,
                                   @RequestPart("arrival_station") int arrival_station_id) {

        Float distance = stationService.calculateDistance(String.valueOf(starting_station_id), String.valueOf(arrival_station_id));

        return distance;
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

    /**
     * 대여소 검색
     * @param station_address
     * @return
     */
    // TODO: 대여소 검색: 이름으로 -> 결과는 아이디랑 이름만 출력
    @GetMapping("/station/search")
    public List<SearchStationRes> searchStationByAddress(@RequestParam("station_address") String station_address){
        return stationService.searchStationByAddress(station_address);
    }

    // TODO: 고장 신고 -> BikeController에 작성

    /**
     * 관리자: 따릉이 대여 빈도 수 높은 곳
     * @return
     */
    @GetMapping("/admin/dashboard/highRentStation")
    public List<Station> getStationByLoanCount() {
        return stationService.getStationByLoanCount();
    }

    /**
     * 관리자: 따릉이 대여 빈도 수 높은 곳(자치구 별)
     * @return
     */
    @GetMapping("/admin/dashboard/borough")
    public List<GetStationBoroughRes> getStationByBorough() {
        return stationService.getStationByBorough();
    }

    /**
     * 관리자: 대여소 폐쇄
     * @param stationId
     * @return
     */
    @DeleteMapping("/admin/station/delete/{stationId}")
    public String deleteStation(@PathVariable int stationId) {

        String deletedStationId = stationService.findByStationId(String.valueOf(stationId)).getId();
        stationService.deleteStation(String.valueOf(stationId));

        return deletedStationId;
    }


}
