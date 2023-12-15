package com.spring.databasebike.domain.post.controller;
import com.spring.databasebike.domain.awsS3.service.AwsS3Service;
import com.spring.databasebike.domain.post.entity.Post;
import com.spring.databasebike.domain.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AwsS3Service awsS3Service;

    //게시판 전체 글 조회 화면 - 문의 게시판
    @GetMapping("/posts") //페이징 처리
    public List<Post> postList(String page, @RequestPart(value = "search", required = false) String searchKeyword){
        int begin, end, nowPage, pageSize = 10;

        List <Post> list = null;

        if ( page == null || page.equals("")) {
            nowPage = 1;
        } else {
            nowPage = Integer.parseInt(page);
        }

        // 현재 페이지에서 가져올 시작 위치 구하기
        begin = ( (nowPage - 1) * pageSize ) + 1;
        // 게시물 끝 위치 찾기
        end = begin + pageSize - 1;

        if(searchKeyword == null) //검색하지 않고 조회
            list = postService.findAllPost(begin, end);
        else list = postService.findSearchPost(begin, end, searchKeyword); //검색하여 조회

        int totalPost = list.size(); //전체 게시글 수

        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, totalPost/pageSize + 1);

        return list;
    }

    //게시판 글쓰기 폼
    /*@GetMapping("/posts/write")
    public String postWriteForm(){
        return "posts/postWrite";
    }*/

    @PostMapping("/posts/write") //작성한 게시글 저장
    public void postWrite(@RequestPart("post") Post p, @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws Exception{
        if(multipartFile!=null)
            p.setFileName(awsS3Service.uploadImage(multipartFile));
        postService.writePost(p);
    }

    //특정 게시글 조회 화면
    @GetMapping("/posts/view")
    public Post postView(int post_id){
        Post post = postService.findByPostId(post_id);
        return post;
    }

    //게시판 글 수정 화면
    @GetMapping("/posts/modify")
    public Optional<Post> postModify(String user_id, int post_id){ //해당 유저가 게시글 작성자랑 동일한지 확인
        Optional<Post> post = postService.findByPostUserId(user_id, post_id);
        return post;
    }

    @PostMapping("/posts/update")
    public void postUpdate(int post_id, @RequestPart("post") Post p, @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws Exception{
        Post post = postService.findByPostId(post_id); //현재 게시글 가져오기
        post.setTitle(p.getTitle());
        post.setContent(p.getContent());

        if(!multipartFile.isEmpty()) {
            p.setFileName(awsS3Service.uploadImage(multipartFile));
            post.setFileName(p.getFileName());
        }
        else {
            awsS3Service.deleteImage(post.getFileName()); //지우기
            post.setFileName(null);
        }
        post.setFilePath(p.getFilePath());
        postService.updatePost(post);
    }

    //게시글 삭제
    @GetMapping("/posts/delete")
    public void postDelete(String user_id, int post_id) {
        Optional<Post> post = postService.findByPostUserId(user_id, post_id);
        if(!post.isEmpty()){
            postService.deletePost(post.get().getPost_id());
        }
    }


    //마이 페이지 - (해당 유저에 대한) 작성글 조회
    //@GetMapping("/members/page/post") //페이징 처리
    @GetMapping("/posts/members")
    public List<Post> getMemPost(@RequestParam("user_id") String id, String page){

        int begin, end, nowPage, pageSize = 10;

        List <Post> list = null;

        if ( page == null || page.equals("")) {
            nowPage = 1;
        } else {
            nowPage = Integer.parseInt(page);
        }

        // 현재 페이지에서 가져올 시작 위치 구하기
        begin = ( (nowPage - 1) * pageSize ) + 1;
        // 게시물 끝 위치 찾기
        end = begin + pageSize - 1;

        int totalPost = postService.memPostNum(id);

        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, totalPost/pageSize + 1);

        list = postService.memPostList(id, begin, end);

        /*for(Post data: list){
            System.out.println(data.getTitle());
        }*/


        return list; //해당 유저가 쓴 게시글 조회 화면
    }

    //관리자 페이지 - 최근 게시글 5개 조회
    @GetMapping("/admin/dashboard/recentPost")
    public List<Post> recentPost(){
        List <Post> list = postService.recentPost();
        return list;
    }

    //관리자 페이지 - 게시글 검색 및 조회
    @GetMapping("/admin/post_manage") //페이징 처리
    public List<Post> AdminpostList(String page, @RequestPart(value = "search", required = false) String searchKeyword){
        int begin, end, nowPage, pageSize = 10; //수정 - 10으로

        List <Post> list = null;

        if ( page == null || page.equals("")) {
            nowPage = 1;
        } else {
            nowPage = Integer.parseInt(page);
        }

        // 현재 페이지에서 가져올 시작 위치 구하기
        begin = ( (nowPage - 1) * pageSize ) + 1;
        // 게시물 끝 위치 찾기
        end = begin + pageSize - 1;

        if(searchKeyword == null) //검색하지 않고 조회
            list = postService.findAllPost(begin, end);
        else list = postService.findSearchPost(begin, end, searchKeyword); //검색하여 조회

        int totalPost = list.size(); //전체 게시글 수

        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, totalPost/pageSize + 1);

        return list;
    }


}
