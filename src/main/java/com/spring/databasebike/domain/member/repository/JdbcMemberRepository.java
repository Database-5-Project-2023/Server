package com.spring.databasebike.domain.member.repository;

import com.spring.databasebike.domain.member.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
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
        String sql = "DELETE FROM comment WHERE creator_id = ?";
        jdbcTemplate.update(sql, id);
        sql = "DELETE FROM post WHERE creator_id = ?";
        jdbcTemplate.update(sql, id);
        sql = "DELETE FROM bookmarks WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
        sql = "DELETE FROM usage_history WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
        sql = "DELETE FROM members where user_id = ?";
        jdbcTemplate.update(sql, id);
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
    public List<Rank> getRanking(String period) {
        String sql = "";
        if(period.equals("month")){
            sql = "select rank() over(order by d.distance desc) as ranking, d.user_id as user_id, d.distance as distance\n" +
                    "from ( select g.user_id, round(sum(g.distance), 1) as distance\n" +
                    "\t\tfrom (SELECT usage_history.user_id, usage_history.distance, usage_history.arrival_time\n" +
                    "\t\t\t\tFROM usage_history join members ON usage_history.user_id = members.user_id\n" +
                    "                ) g\n" +
                    "\t\twhere date(g.arrival_time) >= date_sub(now(), interval 1 month)\n" +
                    "\t\tgroup by(g.user_id) \n" +
                    ") d;";
        }
        else if(period.equals("week")){
            sql = "select rank() over(order by d.distance desc) as ranking, d.user_id as user_id, d.distance as distance\n" +
                    "from ( select g.user_id, round(sum(g.distance), 1) as distance\n" +
                    "\t\tfrom (SELECT usage_history.user_id, usage_history.distance, usage_history.arrival_time\n" +
                    "\t\t\t\tFROM usage_history join members ON usage_history.user_id = members.user_id\n" +
                    "                ) g\n" +
                    "\t\twhere date(g.arrival_time) >= date_sub(now(), interval 7 day)\n" +
                    "\t\tgroup by(g.user_id) \n" +
                    ") d;";
        }
        List<Rank> rankList = jdbcTemplate.query(sql, RankRowMapper());
        return rankList;
    }

    @Override
    public List<Rank> getRankingGender(String period, String gender) {
        String sql = "";
        if(period.equals("month")){
            sql = "select rank() over(order by d.distance desc) as ranking, d.user_id as user_id, d.distance as distance\n" +
                    "from ( select g.user_id, round(sum(g.distance), 1) as distance\n" +
                    "\t\tfrom (SELECT usage_history.user_id, usage_history.distance, usage_history.arrival_time\n" +
                    "\t\t\t\tFROM usage_history join members ON usage_history.user_id = members.user_id\n" +
                    "                where members.gender = ?\n" +
                    "                ) g\n" +
                    "\t\twhere date(g.arrival_time) >= date_sub(now(), interval 1 month)\n" +
                    "\t\tgroup by(g.user_id) \n" +
                    ") d;";
        }
        else if(period.equals("week")){
            sql = "select rank() over(order by d.distance desc) as ranking, d.user_id as user_id, d.distance as distance\n" +
                    "from ( select g.user_id, round(sum(g.distance), 1) as distance\n" +
                    "\t\tfrom (SELECT usage_history.user_id, usage_history.distance, usage_history.arrival_time\n" +
                    "\t\t\t\tFROM usage_history join members ON usage_history.user_id = members.user_id\n" +
                    "                where members.gender = ?\n" +
                    "                ) g\n" +
                    "\t\twhere date(g.arrival_time) >= date_sub(now(), interval 7 day)\n" +
                    "\t\tgroup by(g.user_id) \n" +
                    ") d;";
        }
        List<Rank> rankList = jdbcTemplate.query(sql, RankRowMapper(), gender);
        return rankList;
    }

    @Override
    public List<Rank> getRankingBorough(String period, String borough) {
        String sql = "";
        if(period.equals("month")){
            sql = "select rank() over(order by d.distance desc) as ranking, d.user_id as user_id, d.distance as distance\n" +
                    "from ( select g.user_id, round(sum(g.distance), 1) as distance\n" +
                    "\t\tfrom (SELECT usage_history.user_id, usage_history.distance, usage_history.arrival_time\n" +
                    "\t\t\t\tFROM usage_history join members ON usage_history.user_id = members.user_id\n" +
                    "                where members.address like ?\n" +
                    "                ) g\n" +
                    "\t\twhere date(g.arrival_time) >= date_sub(now(), interval 1 month)\n" +
                    "\t\tgroup by(g.user_id) \n" +
                    ") d;";
        }
        else if(period.equals("week")){
            sql = "select rank() over(order by d.distance desc) as ranking, d.user_id as user_id, d.distance as distance\n" +
                    "from ( select g.user_id, round(sum(g.distance), 1) as distance\n" +
                    "\t\tfrom (SELECT usage_history.user_id, usage_history.distance, usage_history.arrival_time\n" +
                    "\t\t\t\tFROM usage_history join members ON usage_history.user_id = members.user_id\n" +
                    "                where members.address like ?\n" +
                    "                ) g\n" +
                    "\t\twhere date(g.arrival_time) >= date_sub(now(), interval 7 day)\n" +
                    "\t\tgroup by(g.user_id) \n" +
                    ") d;";
        }
        List<Rank> rankList = jdbcTemplate.query(sql, RankRowMapper(), "%" + borough + "%");
        return rankList;
    }

    @Override
    public List<Rank> getRankingAge(String period, Integer age) {
        String sql = "";
        if(period.equals("month")){
            sql = "select rank() over(order by d.distance desc) as ranking, d.user_id as user_id, d.distance as distance\n" +
                    "from ( select g.user_id, round(sum(g.distance), 1) as distance\n" +
                    "\t\tfrom (SELECT usage_history.user_id, usage_history.distance, usage_history.arrival_time\n" +
                    "\t\t\t\tFROM usage_history join members ON usage_history.user_id = members.user_id\n" +
                    "                where members.age >= ? and members.age < ?\n" +
                    "                ) g\n" +
                    "\t\twhere date(g.arrival_time) >= date_sub(now(), interval 1 month)\n" +
                    "\t\tgroup by(g.user_id) \n" +
                    ") d;";
        }
        else if(period.equals("week")){
            sql = "select rank() over(order by d.distance desc) as ranking, d.user_id as user_id, d.distance as distance\n" +
                    "from ( select g.user_id, round(sum(g.distance), 1) as distance\n" +
                    "\t\tfrom (SELECT usage_history.user_id, usage_history.distance, usage_history.arrival_time\n" +
                    "\t\t\t\tFROM usage_history join members ON usage_history.user_id = members.user_id\n" +
                    "                where members.age >= ? and members.age < ?\n" +
                    "                ) g\n" +
                    "\t\twhere date(g.arrival_time) >= date_sub(now(), interval 7 day)\n" +
                    "\t\tgroup by(g.user_id) \n" +
                    ") d;";
        }
        List<Rank> rankList = jdbcTemplate.query(sql, RankRowMapper(), age, age+10);
        return rankList;
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

    @Override
    public Integer getTotalMember() {
        String sql = "select count(*) from members";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public HashMap<Integer, Integer> getMemGraph() {
        HashMap<Integer, Integer> list = new HashMap<>();

        for(int i = 1; i <= 12; i++){
            String s;
            if(i<10)
                    s = "2023-0" + Integer.toString(i);
            else
                s = "2023-" + Integer.toString(i);

            String sql = "select count(*) from members where date_format(created_at, '%Y-%m') = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, s);
            list.put(i, count);
        }
        return list;
    }

    @Override
    public List<Member> getTotalMem() {
        String sql = "SELECT * \n" +
                "FROM (\n" +
                "\t\tSELECT ROW_NUMBER() OVER (ORDER BY user_name) AS NUM, N.*\n" +
                "\t\tFROM (\n" +
                "\t\t\t\tSELECT * FROM members\n" +
                "\t\t) N\n" +
                ")T;";
        return jdbcTemplate.query(sql, MemberRowMapper());
    }

    @Override
    public List<MemberList> getPostComment(String id) {
        String sql = "SELECT title, content\n" +
                "FROM post\n" +
                "WHERE creator_id = ?\n" +
                "UNION\n" +
                "SELECT post_id, content\n" +
                "FROM comment\n" +
                "WHERE creator_id = ?;";
        List<MemberList> memberLists = jdbcTemplate.query(sql, MemberListRowMapper(), id, id);

        return memberLists;

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
            member.setGender(rs.getString("gender"));
            member.setBike_borrow_status(rs.getBoolean("bike_borrow_status"));
            member.setUser_status(rs.getBoolean("user_status"));
            member.setCreated_at(rs.getTimestamp("created_at"));
            member.setDistance(rs.getFloat("distance"));
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

    private RowMapper<Rank> RankRowMapper() {
        return (rs, rowNum) -> {
            Rank rank = new Rank();
            rank.setRank(rs.getInt("ranking"));
            rank.setUser_id(rs.getString("user_id"));
            rank.setDistance(rs.getFloat("distance"));
            return rank;
        }; }

    private RowMapper<MemberList> MemberListRowMapper() {
        return (rs, rowNum) -> {
            MemberList result = new MemberList();
            result.setTitle(rs.getString("title"));
            result.setContent(rs.getString("content"));
            return result;
        }; }
}
