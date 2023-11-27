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

    //게시판 글 조회 화면
    @GetMapping("/posts") //페이징 처리
    public String postList(String page, Model model){
        int begin, end, nowPage, pageSize = 5;

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

        list = postService.findAllPost(begin, end);

        int totalPost = list.size(); //전체 게시글 수

        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, totalPost/pageSize + 1);

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        /*for(Post p: list){
            System.out.println(p.getPost_id() + " " + p.getCreator_id() + " " + p.getTitle() + " " + p.getContent());
        }*/

        return "posts/list";
    }


    //게시판 글쓰기 폼
    @GetMapping("/posts/write")
    public String postWriteForm(){
        return "posts/postWrite";
    }

    @PostMapping("/posts/write") //작성한 게시글 저장
    public String boardWritePro(@RequestPart("post") Post p, @RequestPart("image") MultipartFile multipartFile, Model model) throws Exception{

        p.setFileName(awsS3Service.uploadImage(multipartFile));
        postService.writePost(p); //일단은 이미지빼고 저장되도록 구현

        model.addAttribute("message", "글 작성이 완료되었습니다.");

        return "posts/list";

    }

    //게시판 글 수정 화면


    //마이 페이지 - (해당 유저에 대한)작성글 조회
    @GetMapping("members/page/post") //페이징 처리
    public String getMemPost(String id, String page, Model model){

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

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "members/post"; //해당 유저가 쓴 게시글 조회 화면
    }
}
