package com.spring.databasebike.domain.bike.controller;

import com.spring.databasebike.config.BaseResponse;
import com.spring.databasebike.domain.bike.entity.*;
import com.spring.databasebike.domain.bike.service.BikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class BikeController {

    private final BikeService bikeService;

    @Autowired
    public BikeController(BikeService bikeService){
        this.bikeService = bikeService;
    }

    /**
     * 관리자: 따릉이 추가
     * @param createBikeReq
     * @return
     */
    @PostMapping("/admin/bike/add")
    public String createBike(@RequestBody CreateBikeReq createBikeReq) {

        bikeService.createBike(createBikeReq);

        return createBikeReq.getBike_type();
    }

    /**
     * 따릉이 고장 신고
     * @param reportBikeReq
     * @return
     */
    @PostMapping("/bike/report")
    public BaseResponse<String> reportBike(@RequestBody ReportBikeReq reportBikeReq) {

        bikeService.reportBike(reportBikeReq.getBike_id());

        return new BaseResponse<>(reportBikeReq.getBike_id());
    }

    /**
     * 관리자: 따릉이 관리 페이지 - 따릉이 검색
     * @param searchBikeReq
     * @return
     */
    @GetMapping("/admin/bike_manage")
    public List<SearchBikeRes> searchBikeById(@RequestPart("req") SearchBikeReq searchBikeReq) {
        return bikeService.getListByBikeId(searchBikeReq.getBike_id());
    }

    /**
     * 관리자: 따릉이 관리 페이지 - 따릉이 고장 신고 최신 순 5개
     * @return
     */
    @GetMapping("/admin/dashboard/RecentBikeReport")
    public List<SearchBikeRes> searchBikeByBikeStatus() {
        return bikeService.getListByBikeStatus();
    }
}
