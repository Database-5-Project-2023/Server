package com.spring.databasebike.domain.history.repository;

import com.spring.databasebike.domain.history.entity.CreateHistoryReq;
import com.spring.databasebike.domain.member.entity.History;
import com.spring.databasebike.domain.station.entity.BorrowGeneralBikeReq;
import com.spring.databasebike.domain.station.entity.ReturnGeneralBikeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
public class JdbcHistoryRepository implements HistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcHistoryRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public void createHistory(CreateHistoryReq createHistoryReq) {
        String sql = "INSERT INTO usage_history (user_id, bike_id, starting_station_id, arrival_station_id, starting_time, arrival_time, distance, return_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] createHistoryParams = new Object[] {
                createHistoryReq.getUser_id(),
                createHistoryReq.getBike_id(),
                createHistoryReq.getStarting_station_id(),
                createHistoryReq.getStarting_station_id(),
                createHistoryReq.getStarting_time(),
                LocalDateTime.now(),
                0.0, false
        };

        this.jdbcTemplate.update(sql, createHistoryParams);
    }

    @Override
    public void updateHistory(ReturnGeneralBikeReq returnGeneralBikeReq, String memeberId){
        try {
            History history = findHistoryByBikeId(returnGeneralBikeReq.getBike_id());
            int usage_history_num = Integer.parseInt(history.getUsage_history_num());

            Float distance = returnGeneralBikeReq.getDistance();

            String sql = "UPDATE usage_history SET arrival_station_id = ?, arrival_time = ?, distance = ?, return_status = ? WHERE usage_history_number = ?";
            this.jdbcTemplate.update(sql, returnGeneralBikeReq.getArrival_station_id(), LocalDateTime.now(), distance, true, usage_history_num);
        } catch (Exception e) {
            log.error("[Update History]: 오류 발생", e);
        }
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
        return jdbcTemplate.query(sql, historyRowMapper(), id, start, end);
    }

    @Override
    public List<History> getSearchHistoryList(String id, String period, String start_date, String end_date, int start, int end) {
        String sql = "";
        if(period!=null) {
            if(period.equals("1 week")) {
                sql = "SELECT * FROM (\n" +
                        "    SELECT ROW_NUMBER() OVER (ORDER BY starting_time DESC) AS NUM, N.*\n" +
                        "    FROM (\n" +
                        "        SELECT * FROM usage_history WHERE user_id = ? and date(starting_time) >= date_sub(now(), interval 7 day)\n" +
                        "    ) N\n" +
                        ") AS T\n" +
                        "WHERE NUM BETWEEN ? AND ?;";
            }
            else if(period.equals("1 month")){
                sql = "SELECT * FROM (\n" +
                        "    SELECT ROW_NUMBER() OVER (ORDER BY starting_time DESC) AS NUM, N.*\n" +
                        "    FROM (\n" +
                        "        SELECT * FROM usage_history WHERE user_id = ? and date(starting_time) >= date_sub(now(), interval 1 month)\n" +
                        "    ) N\n" +
                        ") AS T\n" +
                        "WHERE NUM BETWEEN ? AND ?;";
            }
            else if(period.equals("3 month")){
                sql = "SELECT * FROM (\n" +
                        "    SELECT ROW_NUMBER() OVER (ORDER BY starting_time DESC) AS NUM, N.*\n" +
                        "    FROM (\n" +
                        "        SELECT * FROM usage_history WHERE user_id = ? and date(starting_time) >= date_sub(now(), interval 3 month)\n" +
                        "    ) N\n" +
                        ") AS T\n" +
                        "WHERE NUM BETWEEN ? AND ?;";
            }
            else if(period.equals("6 month")){
                sql = "SELECT * FROM (\n" +
                        "    SELECT ROW_NUMBER() OVER (ORDER BY starting_time DESC) AS NUM, N.*\n" +
                        "    FROM (\n" +
                        "        SELECT * FROM usage_history WHERE user_id = ? and date(starting_time) >= date_sub(now(), interval 6 month)\n" +
                        "    ) N\n" +
                        ") AS T\n" +
                        "WHERE NUM BETWEEN ? AND ?;";
            }
            return jdbcTemplate.query(sql, historyRowMapper(), id, start, end);
        }
        else{
            sql = "SELECT * FROM (\n" +
                    "    SELECT ROW_NUMBER() OVER (ORDER BY starting_time DESC) AS NUM, N.*\n" +
                    "    FROM (\n" +
                    "        SELECT * FROM usage_history WHERE user_id = ? and date(starting_time) >= date(?) and date(arrival_time) <= date(?)\n" +
                    "    ) N\n" +
                    ") AS T\n" +
                    "WHERE NUM BETWEEN ? AND ?;";
            return jdbcTemplate.query(sql, historyRowMapper(), id, start_date, end_date, start, end);
        }
    }

    @Override
    public History findHistoryByBikeId(String bikeId){
        List<History> result = jdbcTemplate.query("select * from usage_history where bike_id = ? and return_status = ?", historyRowMapper(), bikeId, false);
        return result.get(0);
    }

    @Override
    public List<History> findHistoryByUserId(String memberId){
        List<History> result = jdbcTemplate.query("select * from usage_history where user_id = ?", historyRowMapper(), memberId);
        return result;
    }

    private RowMapper<History> historyRowMapper() {
        return (rs, rowNum) -> {
            History history = new History();

            history.setUsage_history_num(rs.getString("usage_history_number"));
            history.setUser_id(rs.getString("user_id"));
            history.setBike_id(rs.getString("bike_id"));
            history.setStarting_station_id(rs.getString("starting_station_id"));
            history.setArrival_station_id(rs.getString("arrival_station_id"));
            history.setStarting_time(rs.getTimestamp("starting_time").toLocalDateTime());
            history.setArrival_time(rs.getTimestamp("arrival_time").toLocalDateTime());
            history.setDistance(rs.getDouble("distance"));
            history.setReturn_status(rs.getBoolean("return_status"));

            return history;
        };
    }
}
