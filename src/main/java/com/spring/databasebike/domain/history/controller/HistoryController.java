package com.spring.databasebike.domain.history.controller;

import com.spring.databasebike.domain.history.entity.GetHistoryRes;
import com.spring.databasebike.domain.history.service.HistoryService;
import com.spring.databasebike.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    //마이 페이지 - 대여 및 반납 이력 조회(기간별 조회 가능)
    @GetMapping("/history")
    public List<GetHistoryRes> getMemHistory(String id, String page, @RequestParam(value = "period", required = false) String period, @RequestParam(value="start_date", required = false) String start_date, @RequestParam(value="end_date", required = false) String end_date, Model model){

        //1. 기간 검색 없이 전체 조회
        //2. 1주일, 1개월, 3개월, 6개월 중 하나를 선택하여 조회하고자 하는 경우(period에 1 week, 1 month, 3 month, 6 month로 넣어서 전달)
        //3. start_date와 end_date에 특정 연도와 날짜를 지정하여 검색하는 경우

        int begin, end, nowPage, pageSize = 10;

        List<GetHistoryRes> list = historyService.findHistoryByUserId(id);

        if ( page == null || page.equals("")) {
            nowPage = 1;
        } else {
            nowPage = Integer.parseInt(page);
        }

        // 현재 페이지에서 가져올 시작 위치 구하기
        begin = ( (nowPage - 1) * pageSize ) + 1;
        // 이력의 끝 위치 찾기
        end = begin + pageSize - 1;

        int totalPost = historyService.memHistoryNum(id);

        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, totalPost/pageSize + 1);

        if(period == null && start_date == null && end_date == null) { //기간 검색하지 않는 경우
            list = historyService.memHistoryList(id, begin, end);
        }else { //검색 하는 경우
            list = historyService.memSearchHistoryList(id, period, start_date, end_date, begin, end);
        }

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return list;
    }
}
