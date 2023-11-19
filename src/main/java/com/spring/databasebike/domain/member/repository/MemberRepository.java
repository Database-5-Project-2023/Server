package com.spring.databasebike.domain.member.repository;

import com.spring.databasebike.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

//@Repository
public interface MemberRepository {
    Member save(Member member); //회원가입

    Optional<Member> login(String id, String pwd); //로그인

    Optional<Member> findById(String id);

    void editPwd(String id, String pwd); //비밀번호 수정

    void editEmail(String id, String email); //이메일 수정

    void editPhone(String id, String phone); //전화번호 수정

    void editAddr(String id, String address); //주소 수정

    void editWeight(String id, String weight); //체중 수정

    List<Member> findAll(); //관리자 - 회원 관리

}
