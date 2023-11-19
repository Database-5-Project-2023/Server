package com.spring.databasebike.domain.station.repository;

import com.spring.databasebike.domain.station.entity.Station;
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
public class JdbcStationRepository implements StationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStationRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    @Override
//    public Station save(Station station) {
//        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
//        jdbcInsert.withTableName("station").usingGeneratedKeyColumns("id");
//        Map<String, Object> parameters = new HashMap<>();
//
//        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
//        station.setId(key.longValue());
//        return station;
//    }

    @Override
    public void save(CreateStationReq createStationForm) {
        String sql = "INSERT INTO station (station_id, station_addr1, station_addr2, borough, general_bike, sprout_bike, holder_num, install_date, station_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] createStationParams = new Object[] {
                createStationForm.getStation_id(),
                createStationForm.getStation_addr1(),
                createStationForm.getStation_addr2(),
                createStationForm.getBorough(),
                0, 0, createStationForm.getHolder_num(),
                LocalDateTime.now(), true
        };


        this.jdbcTemplate.update(sql, createStationParams);
    }

    @Override
    public Optional<Station> findById(String stationId) {
        List<Station> result = jdbcTemplate.query("select * from station where station_id = ?", stationRowMapper(), stationId);
        return result.stream().findAny();
    }

    @Override
    public Station findByStationId(String stationId) {
        List<Station> result = jdbcTemplate.query("select * from station where station_id = ?", stationRowMapper(), stationId);
        log.info("The result of station is" + result.get(0).getStation_id());
        return result.get(0);
    }

    @Override
    public Optional<Station> findByBorough(String borough) {
        List<Station> result = jdbcTemplate.query("select * from station where borough = ?", stationRowMapper(), borough);
        return result.stream().findAny();
    }

    @Override
    public List<Station> findAll() {
        return jdbcTemplate.query("select * from station limit 10", stationRowMapper());
    }

    @Override
    public void delete(String stationId) {
        String sql = "DELETE FROM station WHERE station_id = " + stationId;

        this.jdbcTemplate.update(sql);
    }

    private RowMapper<Station> stationRowMapper() {
        return (rs, rowNum) -> {
            Station station = new Station();

            // station.setId(rs.getLong("id"));
            station.setStation_id(rs.getString("station_id"));
            station.setStation_addr1(rs.getString("station_addr1"));
            station.setStation_addr2(rs.getString("station_addr2"));
            station.setBorough(rs.getString("borough"));
            station.setGeneral_bike(rs.getInt("general_bike"));
            station.setSprout_bike(rs.getInt("sprout_bike"));
            station.setHolder_num(rs.getInt("holder_num"));
            station.setInstall_date(rs.getDate("install_date").toLocalDate().atStartOfDay());
            station.setStation_status(rs.getBoolean("station_status"));

            return station;
        };
    }
}
