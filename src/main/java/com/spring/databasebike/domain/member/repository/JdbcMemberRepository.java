package com.spring.databasebike.domain.member.repository;

import com.spring.databasebike.domain.member.entity.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

//@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    public JdbcMemberRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into members(user_id, user_pwd, user_name, user_email, address, user_phone_num, age, gender, weight, bike_borrow_status, user_status) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, member.getId(), member.getPwd(), member.getName(), member.getEmail(), member.getAddress(), member.getPhone_num(), member.getAge(), member.getGender(), member.getWeight(), member.getBike_borrow_status(), member.getUser_status());
        return member;
    }

    @Override
    public Optional<Member> login(String id, String pwd) {
        String sql = "select * from members where user_id = ? and user_pwd = ?";
        try {
            Member findMem = jdbcTemplate.queryForObject(sql, MemberRowMapper(), id, pwd);
            return Optional.of(findMem);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(String id) {
        String sql = "select * from members where user_id = ?";
        try {
            Member findMem = jdbcTemplate.queryForObject(sql, MemberRowMapper(), id);
            return Optional.of(findMem);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void editPwd(String id, String pwd) {
        String sql = "UPDATE members SET user_pwd = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, pwd, id);
    }

    @Override
    public void editEmail(String id, String email) {
        String sql = "UPDATE members SET user_email = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, email, id);
    }

    @Override
    public void editPhone(String id, String phone) {
        String sql = "UPDATE members SET user_phone_num = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, phone, id);
    }

    @Override
    public void editAddr(String id, String address) {
        String sql = "UPDATE members SET address = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, address, id);
    }

    @Override
    public void editWeight(String id, String weight) {
        String sql = "UPDATE members SET weight = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, weight, id);
    }


    @Override
    public List<Member> findAll() { //전체 회원 조회

        return jdbcTemplate.query("select * from members", MemberRowMapper());
    }

    private RowMapper<Member> MemberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getString("user_id"));
            member.setPwd(rs.getString("user_pwd"));
            member.setName(rs.getString("user_name"));
            member.setEmail(rs.getString("user_email"));
            member.setAddress(rs.getString("address"));
            member.setEmail(rs.getString("user_email"));
            member.setPhone_num(rs.getString("user_phone_num"));
            member.setAge(rs.getInt("age"));
            //member.setGender(Gender.valueOf(rs.getString("gender")));
            member.setGender(rs.getString("gender"));
            member.setBike_borrow_status(rs.getBoolean("bike_borrow_status"));
            member.setUser_status(rs.getBoolean("user_status"));
            return member;
        }; }
}
