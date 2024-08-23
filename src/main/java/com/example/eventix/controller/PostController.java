package com.example.eventix.controller;

import com.example.eventix.dto.PostDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/getAllPosts")
    public ResponseEntity<ResponseDTO> getAllPosts() {
        return ResponseEntity.ok().body(postService.getAllPosts());
    }

    @GetMapping("/getPost/{post_id}")
    public ResponseEntity<ResponseDTO> getPost(@PathVariable int post_id) {
        return ResponseEntity.ok().body(postService.getPost(post_id));
    }

    @PostMapping("/savePost")
    public ResponseEntity<ResponseDTO> savePost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok().body(postService.savePost(postDTO));
    }

    @PutMapping("/updatePost/{post_id}")
    public ResponseEntity<ResponseDTO> updatePost(@PathVariable int post_id, @RequestBody PostDTO postDTO) {
        return ResponseEntity.ok().body(postService.updatePost(post_id, postDTO));
    }

    @DeleteMapping("/deletePost/{post_id}")
    public ResponseEntity<ResponseDTO> deletePost(@PathVariable int post_id) {
        return ResponseEntity.ok().body(postService.deletePost(post_id));
    }
}
