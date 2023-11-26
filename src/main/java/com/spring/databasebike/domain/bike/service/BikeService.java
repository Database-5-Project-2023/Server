package com.spring.databasebike.domain.bike.service;

import com.spring.databasebike.domain.bike.entity.CreateBikeReq;
import com.spring.databasebike.domain.bike.repository.BikeRepository;
import com.spring.databasebike.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
