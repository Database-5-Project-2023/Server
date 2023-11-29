package com.spring.databasebike.domain.member.repository;

import com.spring.databasebike.domain.member.entity.Bookmarks;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.member.entity.Member;
import com.spring.databasebike.domain.post.entity.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member); //회원가입

    Optional<Member> login(String id, String pwd); //로그인

    Optional<Member> findById(String id);

    void editPwd(String id, String pwd); //비밀번호 수정

    void editEmail(String id, String email); //이메일 수정

    void editPhone(String id, String phone); //전화번호 수정

    void editAddr(String id, String address); //주소 수정

    void editWeight(String id, String weight); //체중 수정

    Integer getTotalHistory(String id); //해당 유저의 대여/반납 이력의 총 갯수

    List<History> getHistoryList(String id, int start, int end); //해당 유저의 대여/반납 이력 조회

    List<History> getSearchHistoryList(String id, String year, String month, int start, int end); //해당 이용자의 특정 기간동안의 대여/반납 이력 조회

    void deleteMem(String id); //회원 탈퇴

    void addBookmarks(String user_id, String station_id); //해당 이용자의 즐겨찾기 추가

    List <Bookmarks> findBookmarks(String id); //해당 이용자의 즐겨찾기 조회

    void deleteBookmarks(String user_id, String station_id); //해당 이용자의 즐겨찾기 삭제

    List<Member> findAll(int begin, int end); //관리자 - 회원 관리

}
