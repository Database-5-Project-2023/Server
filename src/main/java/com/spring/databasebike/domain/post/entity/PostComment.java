package com.spring.databasebike.domain.post.entity;

import com.spring.databasebike.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PostComment {
    Post post;
    List<Comment> commentList;
}
