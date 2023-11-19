package com.spring.databasebike.domain.bike.service;

import com.spring.databasebike.domain.bike.entity.CreateBikeReq;
import com.spring.databasebike.domain.bike.repository.BikeRepository;
import com.spring.databasebike.domain.station.entity.CreateStationReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;

    @Autowired
    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public void createBike(CreateBikeReq createBikeReq) {
        validateDuplicateBike(createBikeReq);
        bikeRepository.save(createBikeReq);
    }

    private void validateDuplicateBike(CreateBikeReq createBikeReq) {
        bikeRepository.findById(String.valueOf(createBikeReq.getBike_id()))
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 자전거 아이디입니다!!!");
                });
    }
}
