package com.spring.databasebike.domain.member.repository;

import com.spring.databasebike.domain.member.entity.Bookmarks;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.member.entity.Member;
import com.spring.databasebike.domain.post.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
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
    public void deleteMem(String id){
        String sql = "DELETE FROM members WHERE user_id = ?"; //참조 문제,,, ON DELETE CASCADE 추가해주어야 함
        //https://velog.io/@eensungkim/ON-DELETE-CASCADE-feat.-row-%ED%95%9C-%EB%B2%88%EC%97%90-%EC%A7%80%EC%9A%B0%EB%8A%94-%EB%B0%A9%EB%B2%95-TIL-78%EC%9D%BC%EC%B0%A8
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Integer getTotalHistory(String id){
        String sql = "select count(*) from usage_history where user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }

    @Override
    public List<History> getHistoryList(String id, int start, int end) {
        String sql = "SELECT * FROM (\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY starting_time DESC) AS NUM, N.*\n" +
                "    FROM (\n" +
                "        SELECT * FROM usage_history WHERE user_id = ?\n" +
                "    ) N\n" +
                ") AS T\n" +
                "WHERE NUM BETWEEN ? AND ?;";
        return jdbcTemplate.query(sql, HistoryRowMapper(), id, start, end);
    }
    @Override
    public List<History> getSearchHistoryList(String id, String year, String month, int start, int end) {
        String sql = "SELECT * FROM (\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY starting_time DESC) AS NUM, N.*\n" +
                "    FROM (\n" +
                "        SELECT * FROM usage_history WHERE user_id = ? and DATE(starting_time) >= DATE(?) and DATE(arrival_time) <= DATE(?)\n" +
                "    ) N\n" +
                ") AS T\n" +
                "WHERE NUM BETWEEN ? AND ?;";
        return jdbcTemplate.query(sql, HistoryRowMapper(), id, year, month, start, end);
    }

    @Override
    public void addBookmarks(String user_id, String station_id) {
        String sql = "insert into bookmarks(user_id, station_id) values(?, ?)";
        jdbcTemplate.update(sql, user_id, station_id);
    }

    @Override
    public List<Bookmarks> findBookmarks(String id) {
        String sql = "select * from bookmarks where user_id = ?";
        return jdbcTemplate.query(sql, BookmarksRowMapper(), id);
    }

    @Override
    public void deleteBookmarks(String user_id, String station_id) {
        String sql = "DELETE FROM bookmarks WHERE user_id = ? and station_id = ?";
        jdbcTemplate.update(sql, user_id, station_id);
    }

    @Override
    public List<Member> findAll(int begin, int end) { //전체 회원 조회
        String sql = "SELECT * FROM (\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY user_name) AS NUM, N.*\n" +
                "    FROM (\n" +
                "        SELECT * FROM members\n" +
                "    ) N\n" +
                ") AS T\n" +
                "WHERE NUM BETWEEN ? AND ?;";
        return jdbcTemplate.query(sql, MemberRowMapper(), begin, end);
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

    private RowMapper<History> HistoryRowMapper() {
        return (rs, rowNum) -> {
            History history = new History();
            history.setUsage_history_num(rs.getString("usage_history_number"));
            history.setUser_id(rs.getString("user_id"));
            history.setBike_id(rs.getString("bike_id"));
            history.setStarting_station_id(rs.getString("starting_station_id"));
            history.setArrival_station_id(rs.getString("arrival_station_id"));
            history.setStarting_time(rs.getObject("starting_time", LocalDateTime.class));
            history.setArrival_time(rs.getObject("arrival_time", LocalDateTime.class));
            history.setDistance(rs.getDouble("distance"));
            history.setReturn_status(rs.getBoolean("return_status"));
            return history;
        }; }

    private RowMapper<Bookmarks> BookmarksRowMapper() {
        return (rs, rowNum) -> {
            Bookmarks bookmarks = new Bookmarks();
            bookmarks.setUser_id(rs.getString("user_id"));
            bookmarks.setStation_id(rs.getString("station_id"));
            return bookmarks;
        }; }
}
