package com.spring.databasebike.domain.station.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateStationReq {

    private String station_id;

    private String station_addr1;

    private String station_addr2;

    private String borough;

    private int holder_num;

    private int loan_count;
}
