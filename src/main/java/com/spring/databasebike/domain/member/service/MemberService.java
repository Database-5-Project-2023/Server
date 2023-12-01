package com.spring.databasebike.domain.member.service;

import com.spring.databasebike.domain.member.entity.Bookmarks;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.member.entity.Member;
import com.spring.databasebike.domain.member.repository.MemberRepository;
import com.spring.databasebike.domain.post.entity.Post;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

//@Service
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    //회원가입
    public Member join(Member member){
        validateDuplicateMember(member); //중복 회원은 안되니까 검사
        memberRepository.save(member);
        return member;
    }

    private  void validateDuplicateMember(Member member){ //회원가입할때 중복 회원 존재하는지 검사
        memberRepository.findById(member.getId()).ifPresent(m -> {
        throw new IllegalStateException("이미 존재하는 회원입니다.");});
    }
    //로그인
    public Optional<Member>  login(String id, String pwd){
        return memberRepository.login(id, pwd);
    }

    //id로 찾기
    public Optional<Member> findById(String id){
        return memberRepository.findById(id);
    }

    //회원 정보 수정
    public void editPwd(String id, String pwd){
        memberRepository.editPwd(id, pwd);
    }

    public void editEmail(String id, String email){
        memberRepository.editEmail(id, email);
    }

    public void editPhone(String id, String phone){
        memberRepository.editPhone(id, phone);
    }

    public void editAddr(String id, String address){
        memberRepository.editAddr(id, address);
    }

    public void editWeight(String id, String weight){
        memberRepository.editWeight(id, weight);
    }

    //특정 회원의 대여/반납 이력 조회
    public List<History> memHistoryList(String id, int start, int end){
        return memberRepository.getHistoryList(id, start, end);
    }

    //특정 회원의 대여/반납 이력 개수
    public Integer memHistoryNum(String id){
        return memberRepository.getTotalHistory(id);
    }

    //특정 회원의 특정 기간동안의 대여/반납 이력 조회
    public List<History> memSearchHistoryList(String id, String period, String start_date, String end_date, int start, int end){
        return memberRepository.getSearchHistoryList(id, period, start_date, end_date, start, end);
    }

    //회원 탈퇴
    public void deleteMem(String id) { memberRepository.deleteMem(id); }

    //즐겨찾기 추가
    public void addBookmarks(String user_id, String station_id){ memberRepository.addBookmarks(user_id, station_id);}

    //특정 이용자의 즐겨찾기 조회
    public List<Bookmarks> findBookmarks(String id){return memberRepository.findBookmarks(id);}

    //특정 이용자의 특정 즐겨찾기 삭제
    public void deleteBookmarks(String user_id, String station_id){memberRepository.deleteBookmarks(user_id, station_id);}


    //회원 목록 조회
    public List<Member> findAll(int begin, int end){
        return memberRepository.findAll(begin, end);
    }

    public HashMap<Integer, Integer> getMemGraph(){return memberRepository.getMemGraph();}
}
