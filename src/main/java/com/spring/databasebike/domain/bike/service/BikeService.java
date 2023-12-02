package com.spring.databasebike.domain.bike.service;

import com.spring.databasebike.domain.bike.entity.Bike;
import com.spring.databasebike.domain.bike.entity.CreateBikeReq;
import com.spring.databasebike.domain.bike.entity.SearchBikeRes;
import com.spring.databasebike.domain.bike.repository.BikeRepository;
import com.spring.databasebike.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BikeService {

    private final BikeRepository bikeRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public BikeService(BikeRepository bikeRepository, MemberRepository memberRepository) {
        this.bikeRepository = bikeRepository;
        this.memberRepository = memberRepository;
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

    public void reportBike(String bikeId) {
        bikeRepository.updateBikeStatus(bikeId);
    }

    public List<SearchBikeRes> getListByBikeId(String bikeId) {
        List<Bike> result = bikeRepository.getListByBikeId(bikeId);

        List<SearchBikeRes> bikeResList = new ArrayList<>();

        for(Bike bike: result){
            SearchBikeRes bikeRes = new SearchBikeRes();
            bikeRes.setBike_id(bike.getBike_id());
            bikeRes.setStation_id(bike.getStation_id());
            bikeRes.setBike_type(bike.getBike_type());
            bikeRes.setBike_status(String.valueOf(bike.isBike_status()));
            bikeResList.add(bikeRes);
        }

        return bikeResList;
    }
}
