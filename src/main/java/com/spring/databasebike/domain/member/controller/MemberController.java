package com.spring.databasebike.domain.member.controller;

import com.spring.databasebike.domain.member.entity.Bookmarks;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.member.entity.Member;
import com.spring.databasebike.domain.member.service.MemberService;
import com.spring.databasebike.domain.post.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class MemberController {

    @Autowired

    private HttpSession httpSession;

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    //회원가입
    /*@GetMapping("/members/join")
    public String joinForm(){
        return "members/createMemberForm";
    }*/

    @PostMapping("/members/join")
    public Member create(@RequestBody Member member){
        Member newMem = new Member();
        newMem.setId(member.getId());
        newMem.setPwd(member.getPwd());
        newMem.setName(member.getName());
        newMem.setEmail(member.getEmail());
        newMem.setAddress(member.getAddress());
        newMem.setPhone_num(member.getPhone_num());
        newMem.setAge(member.getAge());
        newMem.setGender(member.getGender());
        newMem.setWeight(member.getWeight());

        newMem.setBike_borrow_status(Boolean.FALSE); //default
        newMem.setUser_status(Boolean.FALSE); //default

        Member mem = memberService.join(newMem);

        return mem;
    }

    @PostMapping("members/login")
    public Optional<Member> login(String id, String pwd, HttpServletRequest request) {
        Optional<Member> mem = memberService.login(id, pwd);
        if(!mem.isEmpty()){
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute("user_id", mem.get().getId());
            httpSession.setMaxInactiveInterval(60*60); // 세션 유지기간 60분
        }
        return mem; //만약 로그인이 안되면, null 반환됨.
    }

    //로그아웃
    @GetMapping("members/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    //마이페이지 - 회원 정보 수정 페이지
    /*@GetMapping("members/edit") //주소 수정
    public Optional<Member> editForm(String id, Model model){
        Optional<Member> mem = memberService.findById(id); //일치하는 id를 가진 회원 정보를 가져와
        return mem;
        //model.addAttribute("member", mem);

        //return "members/edit"; //페이지는 그대로. 회원 정보 수정 페이지로
    }*/

    //마이페이지 - 회원 정보 수정 페이지 - 아이디를 제외한 정보 수정
    @PostMapping("members/edit") //주소 수정
    public Optional<Member> editMem(String id, String newPwd, String newEmail, String newPhone,
                          String newAddr, String newWeight){
        if(newPwd!=null) {
            memberService.editPwd(id, newPwd);
        }
        if(newEmail!=null) {
            memberService.editEmail(id, newEmail);
        }
        if(newPhone!=null) {
            memberService.editPhone(id, newPhone);
        }
        if(newAddr!=null) {
            memberService.editAddr(id, newAddr);
        }
        if(newWeight!=null) {
            memberService.editWeight(id, newWeight);
        }
        Optional<Member> mem = memberService.findById(id);
        return mem; //수정한 이용자 반환
    }

    //마이 페이지 - 대여 및 반납 이력 조회 - 수정 필요함...
    @GetMapping("/history")
    public List<History> getMemHistory(String id, String page, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month, Model model){

        int begin, end, nowPage, pageSize = 10;

        List<History> list = null;

        if ( page == null || page.equals("")) {
            nowPage = 1;
        } else {
            nowPage = Integer.parseInt(page);
        }

        // 현재 페이지에서 가져올 시작 위치 구하기
        begin = ( (nowPage - 1) * pageSize ) + 1;
        // 이력의 끝 위치 찾기
        end = begin + pageSize - 1;

        int totalPost = memberService.memHistoryNum(id);

        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, totalPost/pageSize + 1);

        if(year == null && month == null) { //검색하지 않는 경우
            list = memberService.memHistoryList(id, begin, end);
        }else { //검색 하는 경우
            list = memberService.memSearchHistoryList(id, year, month, begin, end);
        }

        /*for(History data: list){
            System.out.println(data.getUsage_history_num() + " " + data.getBike_id() + " " + data.getStarting_station_id() + " " + data.getArrival_station_id() + " " + data.getStarting_time() + " " + data.getArrival_time());
        }*/

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return list;
    }

    //마이 페이지 - 작성글 조회 - PostController에 구현

    //마이 페이지 - 댓글 조회

    //마이 페이지 - 랭킹 조회

    //마이 페이지 - 탈퇴
    @GetMapping("members/delete")
    public Optional<Member> deleteForm(String id, Model model){
        Optional<Member> mem = memberService.findById(id); //일치하는 id를 가진 회원 정보를 가져와
        return mem;
    }

    //마이페이지 - 회원 탈퇴 페이지
    @PostMapping("members/delete")
    public void deleteMem(String id, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        memberService.deleteMem(id);
    }

    //즐겨찾기 - 즐겨찾기 추가
    @PostMapping("/bookmarks/add")
    public void addBookmarks(String user_id, String station_id){
        memberService.addBookmarks(user_id, station_id);
    }

    //즐겨찾기 조회 - 특정 이용자의 즐겨찾기 조회
    @GetMapping("/bookmarks")
    public List<Bookmarks> findBookmarks(String user_id){
        List<Bookmarks> list = memberService.findBookmarks(user_id);
        return list;
    }

    //즐겨찾기 삭제 - 특정 이용자의 즐겨찾기 삭제
    @PostMapping("/bookmarks/delete")
    public void deleteBookmarks(String user_id, String station_id){
        memberService.deleteBookmarks(user_id, station_id);
    }

    // <관리자>

    //1. 회원 관리 페이지 - 회원 조회 및 검색- findAll or 검색함수(검색은 id로)
    @GetMapping("/admin/user_manage")
    public List<Member> AdminUserList(String page, Model model, @RequestPart(value = "search", required = false) String searchKeyword){
        int begin, end, nowPage, pageSize = 10;

        if ( page == null || page.equals("")) {
            nowPage = 1;
        } else {
            nowPage = Integer.parseInt(page);
        }

        // 현재 페이지에서 가져올 시작 위치 구하기
        begin = ( (nowPage - 1) * pageSize ) + 1;
        // 게시물 끝 위치 찾기
        end = begin + pageSize - 1;

        int startPage, endPage;

        if(searchKeyword == null) //검색하지 않고 조회
        {
            List <Member> list = null;
            list = memberService.findAll(begin, end);
            int totalMember = list.size(); //전체 게시글 수
            startPage = Math.max(nowPage - 4, 1);
            endPage = Math.min(nowPage + 5, totalMember / pageSize + 1);
            model.addAttribute("list", list);
            return list;
        }
        else {
            List <Member> list = new ArrayList<Member>();
            Optional <Member> member;
            member = memberService.findById(searchKeyword); //검색하여 조회
            if(!member.isEmpty()) {
                Member mem = member.get();
                list.add(mem);
                model.addAttribute("list", list);
            }
            else model.addAttribute("message", "존재하지 않는 이용자입니다.");
            startPage = 1;
            endPage = 1;
            return list;
        }
        /*model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);*/
    }

    //2. 월 별 회원 가입수 그래프 - column 추가..





}
