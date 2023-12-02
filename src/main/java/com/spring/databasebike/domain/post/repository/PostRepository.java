package com.spring.databasebike.domain.post.repository;

import com.spring.databasebike.domain.post.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    void save(Post p); //게시글 저장

    List<Post> getPostList(int begin, int end); //전체 게시글 조회

    List<Post> getPostListByKeyword(int begin, int end, String searchKeyword); //전체 게시글 조회 중 제목 검색

    Integer getTotalPost(String id); //해당 유저가 작성한 게시글 수를 얻어내는 것
    List<Post> getUserPostList(String id, int start, int end); //특정 이용자가 쓴 게시글 조회

    Post findByPostNum(int num); //특정 게시글 조회(게시글 1개 조회)

    Optional<Post> findByPostUserId(String user_id, int num); //특정 게시글이 해당 유저가 쓴 글이라면, 가져오기

    void updatePost(Post p); //게시글 수정

    void deletePost(int post_id); //게시글 삭제

    List<Post> recentPost(); //최근 게시글 5개 조회


}
