package com.spring.databasebike.domain.post.entity;

import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Table(name = "post")
@Getter
@Setter
public class Post {
    @Id
    private Integer post_id;

    private String creator_id;

    private String title;

    private String content;

    private String fileName;

    private String filePath; //안쓰는데...지워도 되려나?

    private Integer hit;

    private Timestamp created_at;
}
