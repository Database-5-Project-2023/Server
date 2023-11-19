package com.spring.databasebike.domain.member.service;

import com.spring.databasebike.domain.member.entity.Member;
import com.spring.databasebike.domain.member.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//@Service
@Transactional
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

    private  void validateDuplicateMember(Member member){
        memberRepository.findById(member.getId()).ifPresent(m -> {
        throw new IllegalStateException("이미 존재하는 회원입니다.");});
    }
    //로그인
    public Optional<Member>  login(String id, String pwd){
        return memberRepository.login(id, pwd);
    }

    public Optional<Member> findById(String id){
        return memberRepository.findById(id);
    }

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

    //회원 목록 조회
    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    /*public List<History> historyList(Pageable pageable){
        return ...;
    }
    */

}
