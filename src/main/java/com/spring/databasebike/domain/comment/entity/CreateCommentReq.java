package com.spring.databasebike.domain.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateCommentReq {
    private String creator_id;
    // private int post_id;
    private String content;
}
