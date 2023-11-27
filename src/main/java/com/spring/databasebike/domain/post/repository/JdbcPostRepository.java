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
        String uuid = "";
        String file_name = "";
        String file_path = "";

        /*if(!file.isEmpty()){
            uuid = UUID.randomUUID().toString();
            file_name = uuid + file.getOriginalFilename();
            file_path = "img/";
        }*/

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
