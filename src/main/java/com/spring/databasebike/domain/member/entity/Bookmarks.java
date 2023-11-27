package com.spring.databasebike.domain.member.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "bookmarks")
@Getter
@Setter
@ToString
public class Bookmarks {
    private String user_id;
    private String station_id;
}
