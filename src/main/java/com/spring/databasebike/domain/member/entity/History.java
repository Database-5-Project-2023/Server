package com.spring.databasebike.domain.member.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usage_history")
@Getter
@Setter
@ToString
public class History {
    @Id
    @Column(name="user_history_number")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String num;

    // Many = History, One = Member 한명의 회원은 여러 이용 내역을 가질 수 있으니까
    @ManyToOne
    @JoinColumn(name="user_id")
    private Member member;

    /*@ManyToOne
    @JoinColumn(name="bike_id")
    private Bike bike;

    @ManyToOne
    @JoinColumn(name="station_id") //시작, 끝 둘다 이거 하나로 가능?
    private Station station;

    @Column(name="starting_time")
    private LocalDateTime start_time;*/

    @Column(name="arrival_time")
    private LocalDateTime arrival_time;

    @Column(name="distance")
    private double distance;

    @Column(name="return_status")
    private Boolean return_status;



}
