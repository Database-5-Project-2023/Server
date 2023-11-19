package com.spring.databasebike.config;

import com.spring.databasebike.domain.member.repository.JdbcMemberRepository;
import com.spring.databasebike.domain.member.repository.MemberRepository;
import com.spring.databasebike.domain.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {
    private final DataSource dataSource;

    public JdbcTemplateConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new JdbcMemberRepository(dataSource);
    }
}
