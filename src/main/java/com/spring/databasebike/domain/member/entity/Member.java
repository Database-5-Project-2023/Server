package com.spring.databasebike.domain.member.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.List;

@Data
@Table(name = "members")
@Getter
@Setter
public class Member {

    @Id
    private String id;

    private String pwd;

    private String name;

    private String email;

    private String address;

    private String phone_num;

    private Integer age;

    private String gender;

    private Integer weight;

    private Boolean bike_borrow_status;

    private Boolean user_status;

    private Timestamp created_at;

    @MappedCollection(idColumn = "user_id", keyColumn = "usage_history_num")
    private List<History> history;

    @MappedCollection(idColumn = "user_id", keyColumn = "user_id")
    private List<Bookmarks> bookmarks;


}
