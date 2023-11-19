package com.spring.databasebike.domain.member.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "members")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name="user_id", unique = true)
    private String id;

    @Column(name="user_pwd")
    private String pwd;

    @Column(name="user_name")
    private String name;

    @Column(name="user_email")
    private String email;

    @Column(name="address")
    private String address;

    @Column(name="user_phone_num")
    private String phone_num;

    @Column(name="age")
    private Integer age;

    //@Enumerated(EnumType.STRING)
    @Column(name="gender")
    private String gender;

    @Column(name="weight")
    private Integer weight;

    @Column(name="bike_borrow_status")
    private Boolean bike_borrow_status;

    @Column(name="user_status")
    private Boolean user_status;

}
