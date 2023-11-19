package com.spring.databasebike.domain.member.controller;

import com.spring.databasebike.domain.member.entity.Member;
import com.spring.databasebike.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

//@Controller
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
    @GetMapping("/members/join")
    public String joinForm(){
        return "members/createMemberForm"; //수정 필요
    }

    @PostMapping("/members/join")
    public String create(@RequestBody Member member){
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

        return "redirect:/";
    }

    //로그인
    @GetMapping("members/login")
    public String loginForm() {
        return "login/loginForm";
    }

    @PostMapping("members/login")
    public String login(String id, String pwd, HttpServletRequest request) {
        Optional<Member> mem = memberService.login(id, pwd);
        if(mem!=null){
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute("user_id", mem.get().getId());
            // 세션 유지기간 60분
            httpSession.setMaxInactiveInterval(60*60);
            return "redirect:/";
        }
        else {
            return "login/loginForm"; //로그인 화면
        }
    }

    //로그아웃
    @GetMapping("members/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    //마이페이지 - 회원 정보 수정 페이지
    @GetMapping("members/page/edit")
    public String editForm(String id, Model model){
        Optional<Member> mem = memberService.findById(id); //일치하는 id를 가진 회원 정보를 가져와

        model.addAttribute("member", mem);

        return "members/edit"; //페이지는 그대로. 회원 정보 수정 페이지로
    }

    //마이페이지 - 회원 정보 수정 페이지 - 아이디를 제외한 정보 수정
    @PostMapping("members/page/edit")
    public String editMem(String id, String newPwd, String newEmail, String newPhone,
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
        return "members/edit"; //페이지는 그대로
    }

    //마이 페이지 - 대여 및 반납 이력 조회
    /*@GetMapping("members/page/record")
    public String memRecord(@RequestParam("user") String id, Model model,
                            @PageableDefault(page = 0, size = 10, sort = "starting_time", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword){
        //날짜 선택 및 검색
        //이용 시간, 이용 거리, 칼로리, 탄소 절감 양
        //이용 내역 출력

        Page<History> list = null;

        if(searchKeyword == null) { //검색하지 않는 경우
            list = (Page<History>) memberService.historyList(pageable);
        }else { //검색 하는 경우
            list = memberService.historySearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "members/record"; //대여 및 반납 이력 페이지
    }*/

    //마이 페이지 - 작성글 조회

    //마이 페이지 - 댓글 조회

    //마이 페이지 - 랭킹 조회

    //마이 페이지 - 탈퇴


    // <관리자>

    //1. 회원 조회 및 검색- findAll




}
