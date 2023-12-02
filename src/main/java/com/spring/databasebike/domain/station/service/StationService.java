package com.spring.databasebike.domain.station.service;

import com.spring.databasebike.domain.bike.entity.Bike;
import com.spring.databasebike.domain.history.entity.CreateHistoryReq;
import com.spring.databasebike.domain.history.repository.HistoryRepository;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.station.entity.*;
import com.spring.databasebike.domain.member.entity.Member;
import com.spring.databasebike.domain.member.repository.MemberRepository;
import com.spring.databasebike.domain.station.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    private final HistoryRepository historyRepository;

    @Autowired
    public StationService(StationRepository stationRepository, MemberRepository memberRepository, HistoryRepository historyRepository) {
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.historyRepository = historyRepository;
    }

    public void createStation(CreateStationReq createStationForm) {

        validateDuplicateStation(createStationForm);
        createStationForm.setLoan_count(0);
        stationRepository.save(createStationForm);
        // return createStationForm.getStation_addr1();
    }

    private void validateDuplicateStation(CreateStationReq createStationReq) {
        stationRepository.findById(createStationReq.getStation_id())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 대여소 아이디입니다!!!");
                });
    }

    public void borrowGeneralBike(BorrowGeneralBikeReq bikeReq, String memberId) {
        Optional<Member> borrower = memberRepository.findById(memberId);

//  BorrowGeneralBikeReq bikeReq = new BorrowGeneralBikeReq();
        //  borrower.ifPresent(member -> {
        //    String borrowerId = member.getId();
        String borrowerId = memberId;
        bikeReq.setBike_id(bikeReq.getBike_id());
        bikeReq.setStarting_station_id(bikeReq.getStarting_station_id());
        bikeReq.setStart_time(LocalDateTime.now().toString());
        stationRepository.borrowGeneralBike(bikeReq, memberId);
        // });

        CreateHistoryReq createHistoryReq = new CreateHistoryReq();
        createHistoryReq.setUser_id(memberId);
        createHistoryReq.setBike_id(bikeReq.getBike_id());
        createHistoryReq.setStarting_station_id(bikeReq.getStarting_station_id());
        createHistoryReq.setStarting_time(LocalDateTime.now());
        createHistoryReq.setReturn_status(false);

        historyRepository.createHistory(createHistoryReq);
    }

    public void returnGeneralBike(ReturnGeneralBikeReq bikeReq, String memberId) {
        Optional<Member> borrower = memberRepository.findById(memberId);

//  BorrowGeneralBikeReq bikeReq = new BorrowGeneralBikeReq();
        //  borrower.ifPresent(member -> {
        //    String borrowerId = member.getId();
        String borrowerId = memberId;
        bikeReq.setBike_id(bikeReq.getBike_id());
        bikeReq.setArrival_station_id(bikeReq.getArrival_station_id());
        bikeReq.setArrival_time(LocalDateTime.now().toString());
        stationRepository.returnGeneralBike(bikeReq, memberId);

        History history = historyRepository.findHistoryByBikeId(bikeReq.getBike_id());
        Float distance = calculateDistance(history.getStarting_station_id(), bikeReq.getArrival_station_id());
        bikeReq.setDistance(distance);
        // });

        historyRepository.updateHistory(bikeReq, memberId);
    }

    public Float calculateDistance(String starting_station_id, String arrival_station_id) {
        StationLocation startingStationLocation = stationRepository.findLocationByStationId(starting_station_id);
        StationLocation arrivalStationLocation = stationRepository.findLocationByStationId(arrival_station_id);

        Float startingLatitude = startingStationLocation.getStation_latitude();
        Float startingLongitude = startingStationLocation.getStation_longitude();

        Float arrivalLatitude = arrivalStationLocation.getStation_latitude();
        Float arrivalLongitude = arrivalStationLocation.getStation_longitude();

        Float distance = (float) Math.sqrt(Math.pow(arrivalLatitude-startingLatitude, 2) + Math.pow(arrivalLongitude-startingLongitude, 2));

        return distance;
    }

    public List<Bike> getBikeListByStationId(String stationId) {
        return stationRepository.getBikeListByStationId(stationId);
    }

    public List<Station> getStationByLoanCount() {
        return stationRepository.getStationByLoanCount();
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

    public List<SearchStationRes> searchStationByAddress(String address) {
        List<Station> result = stationRepository.findByAddress(address);

        List<SearchStationRes> stationResList = new ArrayList<>();

        for(Station station: result){
            SearchStationRes stationRes = new SearchStationRes();
            stationRes.setId(station.getStation_id());
            stationRes.setStation_addr2(station.getStation_addr2());
            stationResList.add(stationRes);
        }

        return stationResList;
    }

    public List<GetStationBoroughRes> getStationByBorough() {
        return stationRepository.findByBorough();
    }

    public List<StationLocation> getStationListByLocation(){
        return stationRepository.getStationListByLocation();
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
