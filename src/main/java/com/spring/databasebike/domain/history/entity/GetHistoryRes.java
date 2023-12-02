package com.spring.databasebike.domain.history.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetHistoryRes {
    private String user_id;

    private String bike_id;

    private String starting_station_addr;

    private String arrival_station_addr;

    private LocalDateTime starting_time;

    private LocalDateTime arrival_time;

    private double distance;

    private Boolean return_status;
}
