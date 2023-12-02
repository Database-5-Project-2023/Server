package com.spring.databasebike.domain.bike.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchBikeRes {
    private String bike_id;
    private String station_id;
    private String bike_type;
    private String bike_status;
}
