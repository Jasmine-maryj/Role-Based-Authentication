package com.dev.rolebasedauthorization.controller;

import com.dev.rolebasedauthorization.entity.Post;
import com.dev.rolebasedauthorization.entity.PostStatus;
import com.dev.rolebasedauthorization.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @PostMapping("create")
    public String createPost(@RequestBody Post post, Principal principal){
        post.setStatus(PostStatus.PENDING);
        post.setUsername(principal.getName());
        postRepository.save(post);
        return "Your post published successfully, admin approval is pending";
    }

    @GetMapping("/approvePost/{postId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String approvePost(@PathVariable Long postId){
        Post post = postRepository.findById(postId).get();
        post.setStatus(PostStatus.APPROVED);
        postRepository.save(post);
        return "Your post has been approved by admin";
    }

    @GetMapping("/approveAllPosts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String approveAllPosts(){
        postRepository.findAll()
                .stream()
                .filter(post -> post.getStatus()
                        .equals(PostStatus.PENDING))
                .forEach(post -> {
                    post.setStatus(PostStatus.APPROVED);
                    postRepository.save(post);
                });
        return "Approved All posts";
    }

    @GetMapping("/rejectPost/{postId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String rejectPost(@PathVariable Long postId){
        Post post = postRepository.findById(postId).get();
        post.setStatus(PostStatus.REJECTED);
        postRepository.save(post);
        return "Thank you for your participation, we encourage you to write better posts";
    }

    @GetMapping("/rejectAllPosts")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String rejectAllPosts(){
        postRepository.findAll()
                .stream()
                .filter(post -> post.getStatus()
                        .equals(PostStatus.PENDING))
                .forEach(post -> {
                    post.setStatus(PostStatus.REJECTED);
                    postRepository.save(post);
        });
        return "Thank you for your participation, ";
    }

    @GetMapping("/viewAll")
    public List<Post> viewAll(){
        return postRepository.findAll()
                .stream()
                .filter(post -> post.getStatus()
                        .equals(PostStatus.APPROVED))
                .collect(Collectors.toList());
    }
}
