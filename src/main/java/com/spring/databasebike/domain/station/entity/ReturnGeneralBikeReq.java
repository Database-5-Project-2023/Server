package com.spring.databasebike.domain.station.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReturnGeneralBikeReq {
    private String bike_id;
    private String arrival_station_id;
    // private String member_id;
    private String arrival_time;
}
