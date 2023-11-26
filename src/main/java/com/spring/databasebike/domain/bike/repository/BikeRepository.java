package com.spring.databasebike.domain.bike.repository;

import com.spring.databasebike.domain.bike.entity.Bike;
import com.spring.databasebike.domain.bike.entity.CreateBikeReq;

import java.util.Optional;


public interface BikeRepository {

    void save(CreateBikeReq createBikeReq);
    void updateBikeStatus(String bikeId);
    Optional<Bike> findById(String bikeId);
    Bike findByBikeId(String bikeId);
}
