package com.spring.databasebike.domain.station.entity;

import com.spring.databasebike.domain.member.entity.Bookmarks;
import com.spring.databasebike.domain.member.entity.History;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Station {

    private Long id;

    private String station_id;

    private String station_addr1;

    private String station_addr2;

    private String borough;

    private int general_bike;

    private int sprout_bike;

    private int holder_num;

    @CreatedDate
    private LocalDateTime install_date;

    private boolean station_status;

    @MappedCollection(idColumn = "starting_station_id", keyColumn = "usage_history_num")
    private List<History> history1;

    @MappedCollection(idColumn = "arrival_station_id", keyColumn = "usage_history_num")
    private List<History> history2;

    @MappedCollection(idColumn = "station_id", keyColumn = "user_id")
    private List<Bookmarks> bookmarks;

    public void setId(Long id) {
        this.id = id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public void setStation_addr1(String station_addr1) {
        this.station_addr1 = station_addr1;
    }

    public void setStation_addr2(String station_addr2) {
        this.station_addr2 = station_addr2;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public void setGeneral_bike(int general_bike) {
        this.general_bike = general_bike;
    }

    public void setSprout_bike(int sprout_bike) {
        this.sprout_bike = sprout_bike;
    }

    public void setHolder_num(int holder_num) {
        this.holder_num = holder_num;
    }

    public void setInstall_date(LocalDateTime install_date) {
        this.install_date = install_date;
    }

    public void setStation_status(boolean station_status) {
        this.station_status = station_status;
    }

}
