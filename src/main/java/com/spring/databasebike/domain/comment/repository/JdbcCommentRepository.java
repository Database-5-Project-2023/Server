package com.spring.databasebike.domain.comment.repository;

import com.spring.databasebike.domain.comment.entity.Comment;
import com.spring.databasebike.domain.comment.entity.CreateCommentReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCommentRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(CreateCommentReq createCommentReq, int postId) {
        String sql = "INSERT INTO comment (creator_id, post_id, content, created_at) VALUES (?, ?, ?, ?)";

        Object[] createCommentParams = new Object[] {
                // createCommentReq.getComment_id(),
                createCommentReq.getCreator_id(),
                postId,
                // createCommentReq.getPost_id(),
                createCommentReq.getContent(),
                LocalDateTime.now()
        };

        this.jdbcTemplate.update(sql, createCommentParams);
    }

    @Override
    public List<Comment> getCommentListByPostId(int post_id) {
        String sql = "SELECT * FROM comment as c LEFT OUTER JOIN post as p ON c.post_id = p.post_id WHERE p.post_id = ? ORDER BY c.created_at DESC";
        List<Comment> result = jdbcTemplate.query(sql, CommentRowMapper(), post_id);
        return result;
    }

    @Override
    public List<Comment> getCommentListByUserId(String user_id) {
        String sql = "SELECT * FROM comment as c WHERE c.creator_id = ? ORDER BY c.created_at DESC";
        List<Comment> result = jdbcTemplate.query(sql, CommentRowMapper(), user_id);
        return result;
    }

    private RowMapper<Comment> CommentRowMapper() {
        return (rs, rowNum) -> {
            Comment comment = new Comment();
            comment.setComment_id(rs.getInt("comment_id"));
            comment.setCreator_id(rs.getString("creator_id"));
            comment.setPost_id(rs.getInt("post_id"));
            comment.setContent(rs.getString("content"));
            comment.setCreated_at(rs.getTimestamp("created_at"));
            return comment;
        };
    }
}
