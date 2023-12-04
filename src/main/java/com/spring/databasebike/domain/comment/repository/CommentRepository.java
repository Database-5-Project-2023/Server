package com.spring.databasebike.domain.comment.repository;

import com.spring.databasebike.domain.comment.entity.Comment;
import com.spring.databasebike.domain.comment.entity.CreateCommentReq;

import java.util.List;

public interface CommentRepository {

    void save(CreateCommentReq createCommentReq, int postId);

    List<Comment> getCommentListByPostId(int post_id);

    List<Comment> getCommentListByUserId(String user_id);
}
