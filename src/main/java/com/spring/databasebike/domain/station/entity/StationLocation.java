package com.spring.databasebike.domain.station.entity;

import lombok.Data;

@Data
public class StationLocation {

    private String station_id;
    private Float station_latitude;
    private Float station_longitude;

}
