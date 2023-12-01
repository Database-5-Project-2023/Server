package com.spring.databasebike.domain.post.repository;

import com.spring.databasebike.domain.member.entity.Member;
import com.spring.databasebike.domain.post.entity.Post;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcPostRepository implements PostRepository{

    private final JdbcTemplate jdbcTemplate;
    public JdbcPostRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void save(Post p) {

        String file_path = ""; //필요한가?

        Long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);

        String sql = "insert into post(creator_id, title, content, hit, created_at, file_name, file_path) values(?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, p.getCreator_id(), p.getTitle(), p.getContent(), 0, timestamp, p.getFileName(), file_path);

    }

    @Override
    public List<Post> getPostList(int begin, int end) {
        String sql = "SELECT * FROM (\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY created_at DESC) AS NUM, N.*\n" +
                "    FROM (\n" +
                "        SELECT * FROM post\n" +
                "    ) N\n" +
                ") AS T\n" +
                "WHERE NUM BETWEEN ? AND ?;";
        return jdbcTemplate.query(sql, PostRowMapper(), begin, end);
    }

    @Override
    public List<Post> getPostListByKeyword(int begin, int end, String searchKeyword) {
        String sql = "SELECT * FROM (\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY created_at DESC) AS NUM, N.*\n" +
                "    FROM (\n" +
                "        SELECT * FROM post where title like ? \n" +
                "    ) N\n" +
                ") AS T\n" +
                "WHERE NUM BETWEEN ? AND ?;";
        return jdbcTemplate.query(sql, PostRowMapper(), "%" + searchKeyword + "%", begin, end);
    }

    @Override
    public Integer getTotalPost(String id){ //해당 유저가 작성한 게시글 수 반환
        String sql = "select count(*) from post where creator_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }

    @Override
    public List<Post> getUserPostList(String id, int start, int end) { //해당 유저가 작성한 게시글 조회
        String sql = "SELECT * FROM (\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY created_at DESC) AS NUM, N.*\n" +
                "    FROM (\n" +
                "        SELECT * FROM post WHERE creator_id = ?\n" +
                "    ) N\n" +
                ") AS T\n" +
                "WHERE NUM BETWEEN ? AND ?;";
        return jdbcTemplate.query(sql, PostRowMapper(), id, start, end);

    }

    @Override
    public Post findByPostNum(int num) {
        String sql = "select * from post limit ?, 1";
        return jdbcTemplate.queryForObject(sql, PostRowMapper(), num);
    }

    @Override
    public Optional<Post> findByPostUserId(String user_id, int num) {
        String sql = "select * from(select * from post limit ?, 1)R where creator_id = ?";
        try {
            Post post = jdbcTemplate.queryForObject(sql, PostRowMapper(), num, user_id);
            return Optional.of(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public void updatePost(Post p) {
        String sql = "UPDATE post SET title = ? WHERE post_id = ?";
        jdbcTemplate.update(sql, p.getTitle(), p.getPost_id());

        sql = "UPDATE post SET content = ? WHERE post_id = ?";
        jdbcTemplate.update(sql, p.getContent(), p.getPost_id());

        sql = "UPDATE post SET title = ? WHERE post_id = ?";
        jdbcTemplate.update(sql, p.getTitle(), p.getPost_id());

        sql = "UPDATE post SET file_name = ? WHERE post_id = ?";
        jdbcTemplate.update(sql, p.getFileName(), p.getPost_id());
    }

    @Override
    public void deletePost(int post_id) {
        String sql = "DELETE FROM post WHERE post_id = ?";
        jdbcTemplate.update(sql, post_id);
    }

    @Override
    public List<Post> recentPost() {
        String sql = "SELECT * FROM (\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY created_at DESC) AS NUM, N.*\n" +
                "    FROM (\n" +
                "        SELECT * FROM post\n" +
                "    ) N\n" +
                ") AS T\n" +
                "WHERE NUM BETWEEN 1 AND 5;";
        return jdbcTemplate.query(sql, PostRowMapper());
    }

    private RowMapper<Post> PostRowMapper() {
        return (rs, rowNum) -> {
            Post post = new Post();
            post.setPost_id(rs.getInt("post_id"));
            post.setCreator_id(rs.getString("creator_id"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setFileName(rs.getString("file_name"));
            post.setFilePath(rs.getString("file_path"));
            post.setHit(rs.getInt("hit"));
            post.setCreated_at(rs.getTimestamp("created_at"));
            return post;
        }; }
}
