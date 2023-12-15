package com.spring.databasebike.domain.member.entity;

import com.spring.databasebike.domain.comment.entity.Comment;
import com.spring.databasebike.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberList {
    String title;
    String content;
}
