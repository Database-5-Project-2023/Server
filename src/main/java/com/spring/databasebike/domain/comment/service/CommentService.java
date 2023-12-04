package com.spring.databasebike.domain.comment.service;

import com.spring.databasebike.domain.comment.entity.Comment;
import com.spring.databasebike.domain.comment.entity.CreateCommentReq;
import com.spring.databasebike.domain.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public void createComment(CreateCommentReq createCommentReq, int post_id){
        CreateCommentReq req = new CreateCommentReq();
        req.setCreator_id(createCommentReq.getCreator_id());
        // req.setPost_id(post_id);
        req.setContent(createCommentReq.getContent());
        commentRepository.save(req, post_id);
    }

    public List<Comment> getCommentListByPostId(int post_id) {
        return commentRepository.getCommentListByPostId(post_id);
    }

    public List<Comment> getCommentListByUserId(String user_id){
        return commentRepository.getCommentListByUserId(user_id);
    }

}
