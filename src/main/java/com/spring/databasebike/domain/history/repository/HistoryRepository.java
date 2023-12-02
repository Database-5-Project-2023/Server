package com.spring.databasebike.domain.history.repository;

import com.spring.databasebike.domain.history.entity.CreateHistoryReq;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.station.entity.BorrowGeneralBikeReq;
import com.spring.databasebike.domain.station.entity.ReturnGeneralBikeReq;

import java.util.List;

public interface HistoryRepository {

    void createHistory(CreateHistoryReq createHistoryReq);

    void updateHistory(ReturnGeneralBikeReq returnGeneralBikeReq, String memberId);

    Integer getTotalHistory(String id); //해당 유저의 대여/반납 이력의 총 갯수

    List<History> getHistoryList(String id, int start, int end);

    List<History> getSearchHistoryList(String id, String period, String start_date, String end_date, int start, int end);

    History findHistoryByBikeId(String bikeId);

    List<History> findHistoryByUserId(String memberId);
}
