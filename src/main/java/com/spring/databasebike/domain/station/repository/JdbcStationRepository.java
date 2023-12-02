package com.spring.databasebike.domain.station.repository;

import com.spring.databasebike.domain.bike.entity.Bike;
import com.spring.databasebike.domain.station.entity.*;
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
        String sql = "INSERT INTO station (station_id, station_addr1, station_addr2, borough, general_bike, sprout_bike, holder_num, install_date, station_status, loan_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] createStationParams = new Object[] {
                createStationForm.getStation_id(),
                createStationForm.getStation_addr1(),
                createStationForm.getStation_addr2(),
                createStationForm.getBorough(),
                0, 0, createStationForm.getHolder_num(),
                LocalDateTime.now(), true, 0
        };

        this.jdbcTemplate.update(sql, createStationParams);
    }

    @Override
    public void borrowGeneralBike(BorrowGeneralBikeReq bikeReq, String memberId) {
        try {
            Station station = findByStationId(bikeReq.getStarting_station_id());
            station.setGeneral_bike(station.getGeneral_bike() - 1);
            station.setLoan_count(station.getLoan_count() + 1);

            String sql = "UPDATE station SET general_bike = ?, loan_count = ? WHERE station_id = ?";
            this.jdbcTemplate.update(sql, station.getGeneral_bike(), station.getLoan_count(), station.getStation_id());

            log.info("자전거 대여 성공 - 스테이션 ID: {}, 현재 보유 자전거 수: {}", station.getStation_id(), station.getGeneral_bike());
        } catch (Exception e) {
            log.error("자전거 대여 중 오류 발생", e);
        }
    }

    @Override
    public void returnGeneralBike(ReturnGeneralBikeReq bikeReq, String memberId) {
        try {
            Station station = findByStationId(bikeReq.getArrival_station_id());
            station.setGeneral_bike(station.getGeneral_bike() + 1);

            String sql = "UPDATE station SET general_bike = ? WHERE station_id = ?";
            this.jdbcTemplate.update(sql, station.getGeneral_bike(), station.getStation_id());

            log.info("자전거 반납 성공 - 스테이션 ID: {}, 현재 보유 자전거 수: {}", station.getStation_id(), station.getGeneral_bike());
        } catch (Exception e) {
            log.error("자전거 반납 중 오류 발생", e);
        }
    }

    @Override
    public List<Bike> getBikeListByStationId(String stationId) {
        List<Bike> result = jdbcTemplate.query("SELECT * FROM station as s INNER JOIN bike as b ON s.station_id = b.station_id WHERE s.station_id = ?", bikeRowMapper(), stationId);
        return result;
    }

    @Override
    public StationLocation findLocationByStationId(String stationId) {
        List<StationLocation> result = jdbcTemplate.query("select * from station_location where station_id = ?", stationLocationRowMapper(), stationId);
        log.info("The result of station is" + result.get(0).getStation_id());
        return result.get(0);
    }

    @Override
    public List<Station> getStationByLoanCount() {
        List<Station> result = jdbcTemplate.query("select * from station order by loan_count DESC limit 10", stationRowMapper());
        return result;
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
    public List<GetStationBoroughRes> findByBorough() {

        String viewName = "station_loan_count";

        if(!checkDuplicateView(viewName)) {
            String sql = "CREATE VIEW station_loan_count (borough, station_loan_count) AS\n"
                    + "SELECT s.borough, SUM(s.loan_count)\n"
                    + "FROM station as s\n"
                    + "GROUP BY s.borough\n"
                    + "ORDER BY SUM(s.loan_count) DESC";

            jdbcTemplate.update(sql);
        }

        String viewSql = "SELECT * FROM station_loan_count";

        List<GetStationBoroughRes> result = jdbcTemplate.query(viewSql, stationBoroughRowMapper());

        return result;
    }

    private boolean checkDuplicateView(String viewName) {
        String sql = "SELECT COUNT(*) FROM information_schema.views WHERE table_name = ?";

        int count = jdbcTemplate.queryForObject(sql, Integer.class, viewName);

        return count != 0;
    }

    @Override
    public List<Station> findByAddress(String address) {
        String sql = "SELECT station_id, station_addr1, station_addr2 FROM station WHERE station_addr2 LIKE ?";

        List<Station> result = jdbcTemplate.query(sql, searchStationRowMapper(), "%" + address + "%");

        return result;
    }

    @Override
    public List<Station> findAll() {
        return jdbcTemplate.query("select * from station limit 10", stationRowMapper());
    }

    @Override
    public List<StationLocation> getStationListByLocation() {
        return jdbcTemplate.query("SELECT s.station_id, s.station_addr1, s.station_addr2, sl.station_latitude, sl.station_longitude " +
                "FROM station as s INNER JOIN station_location as sl ON s.station_id = sl.station_id", stationLocationRowMapper());
    }

    @Override
    public void delete(String stationId) {
        String sql = "DELETE FROM station WHERE station_id = " + stationId;

        this.jdbcTemplate.update(sql);
    }

    private RowMapper<Station> stationRowMapper() {
        return (rs, rowNum) -> {
            Station station = new Station();

            station.setStation_id(rs.getString("station_id"));
            station.setStation_addr1(rs.getString("station_addr1"));
            station.setStation_addr2(rs.getString("station_addr2"));
            station.setBorough(rs.getString("borough"));
            station.setGeneral_bike(rs.getInt("general_bike"));
            station.setSprout_bike(rs.getInt("sprout_bike"));
            station.setHolder_num(rs.getInt("holder_num"));
            station.setInstall_date(rs.getDate("install_date").toLocalDate().atStartOfDay());
            station.setStation_status(rs.getBoolean("station_status"));
            station.setLoan_count(rs.getInt("loan_count"));

            return station;
        };
    }

    private RowMapper<StationLocation> stationLocationRowMapper() {
        return (rs, rowNum) -> {
            StationLocation stationLocation = new StationLocation();

            stationLocation.setStation_id(rs.getString("station_id"));
            stationLocation.setStation_latitude(rs.getFloat("station_latitude"));
            stationLocation.setStation_longitude(rs.getFloat("station_longitude"));

            return stationLocation;
        };
    }

    private RowMapper<Station> searchStationRowMapper() {
        return (rs, rowNum) -> {
            Station station = new Station();

            station.setStation_id(rs.getString("station_id"));
            station.setStation_addr1(rs.getString("station_addr1"));
            station.setStation_addr2(rs.getString("station_addr2"));

            return station;
        };
    }

    private RowMapper<GetStationBoroughRes> stationBoroughRowMapper() {
        return (rs, rowNum) -> {
            GetStationBoroughRes stationBoroughRes = new GetStationBoroughRes();

            stationBoroughRes.setBorough(rs.getString("borough"));
            stationBoroughRes.setLoan_count(rs.getInt("station_loan_count"));

            return stationBoroughRes;
        };
    }

    private RowMapper<Bike> bikeRowMapper() {
        return (rs, rowNum) -> {
            Bike bike = new Bike();

            bike.setBike_id(rs.getString("bike_id"));
            bike.setStation_id(rs.getString("station_id"));
            bike.setBike_type(rs.getString("bike_type"));
            bike.setBike_status(rs.getBoolean("bike_status"));

            return bike;
        };
    }
}
