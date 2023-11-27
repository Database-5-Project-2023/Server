package com.spring.databasebike.domain.bike.entity;

import com.spring.databasebike.domain.member.entity.Bookmarks;
import com.spring.databasebike.domain.member.entity.History;
import lombok.Data;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.List;

@Data
public class Bike {

    // TODO: JdbcTemplate Join -> 어떻게?
    private String bike_id;
    private String station_id;
    private String bike_type;
    private boolean bike_status;

    @MappedCollection(idColumn = "bike_id", keyColumn = "usage_history_num")
    private List<History> history;

    public void setBike_id(String bike_id) {
        this.bike_id = bike_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public void setBike_type(String bike_type) {
        this.bike_type = bike_type;
    }

    public void setBike_status(boolean bike_status) {
        this.bike_status = bike_status;
    }

}
