package com.spring.databasebike.domain.bike.entity;

import lombok.Data;

@Data
public class Bike {

    // TODO: JdbcTemplate Join -> 어떻게?
    private String bike_id;
    private String station_id;
    private String bike_type;
    private boolean bike_status;

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
