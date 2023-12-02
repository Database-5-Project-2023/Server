package com.spring.databasebike.domain.bike.repository;

import com.spring.databasebike.domain.bike.entity.Bike;
import com.spring.databasebike.domain.bike.entity.CreateBikeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JdbcBikeRepository implements BikeRepository {

    private final JdbcTemplate jdbcTemplate;


    public JdbcBikeRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(CreateBikeReq createBikeReq) {
        String sql = "INSERT INTO bike (bike_id, station_id, bike_type, bike_status, updated_at) VALUES (?, ?, ?, ?, ?)";

        Object[] createStationParams = new Object[] {
                String.valueOf(createBikeReq.getBike_id()),
                createBikeReq.getStation_id(),
                createBikeReq.getBike_type(), false, LocalDateTime.now()
        };


        this.jdbcTemplate.update(sql, createStationParams);
    }

//    @Override
//    public void borrowSproutBike(BorrowGeneralBikeReq borrowBikeReq, String memberId){
//        String sql = "UPDATE station SET sprout_bike = sprout_bike - 1 where bike.station_id = station_id";
//
//        jdbcTemplate.update(sql, borrowBikeReq.getBike_id());
//    }

    @Override
    public void updateBikeStatus(String bikeId) {
        try {
            Bike bike = findByBikeId(bikeId);

            if(bike.isBike_status() == false) {
                bike.setBike_status(true);
            } else {
                bike.setBike_status(false);
            }

            String sql = "UPDATE bike SET bike_status = ? WHERE bike_id = ?";

            this.jdbcTemplate.update(sql, bike.isBike_status(), bike.getBike_id());

        } catch (Exception e) {
            log.error("자전거 신고 중 오류 발생", e);
        }
    }

    @Override
    public Optional<Bike> findById(String bikeId) {
        List<Bike> result = jdbcTemplate.query("select * from bike where bike_id = ?", bikeRowMapper(), bikeId);
        return result.stream().findAny();
    }

    @Override
    public Bike findByBikeId(String bikeId) {
        List<Bike> result = jdbcTemplate.query("select * from bike where bike_id = ?", bikeRowMapper(), bikeId);
        return result.get(0);
    }

    @Override
    public List<Bike> getListByBikeId(String bikeId) {
        String sql = "select * from bike where bike_id LIKE ?";

        List<Bike> result = jdbcTemplate.query(sql, bikeRowMapper(), "%" + bikeId + "%");

        return result;
    }

    @Override
    public List<Bike> getListByBikeStatus() {
        String sql = "SELECT station_id, bike_id, bike_type, bike_status " +
                "FROM bike JOIN station using(station_id) " +
                "WHERE bike.bike_status = ? ORDER BY bike.updated_at DESC";

        List<Bike> result = jdbcTemplate.query(sql, bikeRowMapper(), true);

        return result;
    }

    private RowMapper<Bike> bikeRowMapper() {
        return (rs, rowNum) -> {
            Bike bike = new Bike();

            // station.setId(rs.getLong("id"));
            bike.setBike_id(rs.getString("bike_id"));
            bike.setStation_id(rs.getString("station_id"));
            bike.setBike_type(rs.getString("bike_type"));
            bike.setBike_status(rs.getBoolean("bike_status"));

            return bike;
        };
    }
}
