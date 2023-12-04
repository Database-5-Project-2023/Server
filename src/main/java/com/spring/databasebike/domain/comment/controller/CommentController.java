package com.spring.databasebike.domain.comment.controller;

import com.spring.databasebike.domain.comment.entity.Comment;
import com.spring.databasebike.domain.comment.entity.CreateCommentReq;
import com.spring.databasebike.domain.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 작성
     * @param createCommentReq
     * @param post_id
     */
    @PostMapping("/comments")
    public void createComment(@RequestPart("req") CreateCommentReq createCommentReq, @RequestParam("post_id") int post_id) {
        commentService.createComment(createCommentReq, post_id);
    }

    /**
     * 특정 글의 댓글 목록 조회
     * @param post_id
     * @return
     */
    @GetMapping("/comments")
    public List<Comment> getCommentList(@RequestParam("post_id") int post_id) {
        return commentService.getCommentListByPostId(post_id);
    }

    /**
     * 이용자 한 명이 쓴 작성 댓글 목록 조회
     * @param user_id
     * @return
     */
    @GetMapping("/comments/members")
    public List<Comment> getCommentList(@RequestParam("user_id") String user_id) {
        return commentService.getCommentListByUserId(user_id);
    }

}
