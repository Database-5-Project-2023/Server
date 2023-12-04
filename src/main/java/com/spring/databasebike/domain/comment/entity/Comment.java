package com.spring.databasebike.domain.comment.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Data
@Table(name = "comment")
@ToString
public class Comment {
    @Id
    private Integer comment_id;

    private String creator_id;

    private Integer post_id;

    private String content;

    private Timestamp created_at;
}
