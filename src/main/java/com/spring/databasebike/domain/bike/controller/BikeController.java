package com.spring.databasebike.domain.bike.controller;

import com.spring.databasebike.config.BaseResponse;
import com.spring.databasebike.domain.bike.entity.CreateBikeReq;
import com.spring.databasebike.domain.bike.entity.ReportBikeReq;
import com.spring.databasebike.domain.bike.service.BikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/bike/report")
    public BaseResponse<String> reportBike(@RequestBody ReportBikeReq reportBikeReq) {

        bikeService.reportBike(reportBikeReq.getBike_id());

        return new BaseResponse<>(reportBikeReq.getBike_id());
    }

}
