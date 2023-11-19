package com.spring.databasebike.domain.bike.repository;

import com.spring.databasebike.domain.bike.entity.Bike;
import com.spring.databasebike.domain.bike.entity.CreateBikeReq;
import com.spring.databasebike.domain.station.entity.CreateStationReq;
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
        String sql = "INSERT INTO bike (bike_id, station_id, bike_type, station_status) VALUES (?, ?, ?, ?)";

        Object[] createStationParams = new Object[] {

                createBikeReq.getStation_id(),
                createBikeReq.getBike_type(), true
        };


        this.jdbcTemplate.update(sql, createStationParams);
    }

    @Override
    public Optional<Bike> findById(String bikeId) {
        List<Bike> result = jdbcTemplate.query("select * from bike where bike_id = ?", bikeRowMapper(), bikeId);
        return result.stream().findAny();
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
