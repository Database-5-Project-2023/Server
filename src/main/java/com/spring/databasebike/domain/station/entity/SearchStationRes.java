package com.spring.databasebike.domain.station.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchStationRes {
    // TODO: 아이디, station_addr2
    private String id;
    private String station_addr2;
}
