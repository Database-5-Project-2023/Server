package com.spring.databasebike.domain.post.repository;

import com.spring.databasebike.domain.post.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    void save(Post p);

    List<Post> getPostList(int begin, int end);
    Integer getTotalPost(String id); //해당 유저가 작성한 게시글 수를 얻어내는 것

    List<Post> getUserPostList(String id, int start, int end);
}
