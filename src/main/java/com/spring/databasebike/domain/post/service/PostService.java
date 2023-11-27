package com.spring.databasebike.domain.post.service;

import com.spring.databasebike.domain.post.entity.Post;
import com.spring.databasebike.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public List<Post> findAllPost(int begin, int end){
        return postRepository.getPostList(begin, end);
    }

    public void writePost(Post p){
        postRepository.save(p);
    }

    public List<Post> memPostList(String id, int start, int end){
        return postRepository.getUserPostList(id, start, end);
    }

    public Integer memPostNum(String id){
        return postRepository.getTotalPost(id);
    }
}
