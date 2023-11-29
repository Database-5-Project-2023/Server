package com.spring.databasebike.domain.history.service;

import com.spring.databasebike.domain.history.repository.HistoryRepository;
import com.spring.databasebike.domain.member.entity.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository){
        this.historyRepository = historyRepository;
    }

    public List<History> findHistoryByUserId(String memberId){
        return historyRepository.findHistoryByUserId(memberId);
    }

}
