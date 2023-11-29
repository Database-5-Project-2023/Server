package com.spring.databasebike.domain.post.service;

import com.spring.databasebike.domain.post.entity.Post;
import com.spring.databasebike.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    //전체 게시글 조회
    public List<Post> findAllPost(int begin, int end){
        return postRepository.getPostList(begin, end);
    }

    //전체 게시글 조회 중 제목 검색
    public List<Post> findSearchPost(int begin, int end, String searchKeyword){return postRepository.getPostListByKeyword(begin, end, searchKeyword);}

    //게시글 작성
    public void writePost(Post p){
        postRepository.save(p);
    }

    //특정 이용자가 작성한 게시글 조회
    public List<Post> memPostList(String id, int start, int end){
        return postRepository.getUserPostList(id, start, end);
    }

    //특정 이용자가 작성한 전체 게시글 갯수 반환
    public Integer memPostNum(String id){
        return postRepository.getTotalPost(id);
    }

    //n번째 게시글 반환
    public Post findByPostNum(int num){return postRepository.findByPostNum(num);}

    public Optional<Post> findByPostUserId(String user_id, int num){return postRepository.findByPostUserId(user_id, num);}

    public void updatePost(Post p){postRepository.updatePost(p);}

    public void deletePost(int post_id){postRepository.deletePost(post_id);}

    public List<Post> recentPost(){return postRepository.recentPost();}
}
