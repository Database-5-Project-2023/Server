package com.spring.databasebike.domain.history.controller;

import com.spring.databasebike.domain.history.entity.GetHistoryRes;
import com.spring.databasebike.domain.history.service.HistoryService;
import com.spring.databasebike.domain.member.entity.History;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService){
        this.historyService = historyService;
    }

    @GetMapping("/history/{memberId}")
    public List<GetHistoryRes> getHistoryList(@PathVariable String memberId){
        return historyService.findHistoryByUserId(memberId);
    }

}
