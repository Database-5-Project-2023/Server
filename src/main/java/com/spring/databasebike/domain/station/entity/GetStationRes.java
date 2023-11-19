package com.spring.databasebike.domain.station.entity;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetStationRes {

    // TODO: 아이디, station_addr2, 일반 따릉이, 새싹 따릉이
    private String id;
    private String station_addr2;
    private int general_bike;
    private int sprout_bike;
    private int remainder_holder;
}
