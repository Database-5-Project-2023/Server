package com.spring.databasebike.domain.member.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
@Table(name = "usage_history")
@Getter
@Setter
@ToString
public class History {
    @Id
    private String usage_history_num;

    private String user_id;

    private String bike_id;

    private String starting_station_id;

    private String arrival_station_id;

    private LocalDateTime starting_time;

    private LocalDateTime arrival_time;

    private double distance;

    private Boolean return_status;
}
