package com.example.eventix.controller;

import com.example.eventix.dto.Event_SponsorDTO;
import com.example.eventix.dto.PostDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value="/savePost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> savePost(@RequestPart("data") PostDTO postDTO, @RequestPart(value = "file", required = false) MultipartFile file ) {
        System.out.println("Published User ID in post controller: " + postDTO);
        return ResponseEntity.ok().body(postService.savePost(postDTO, file));
    }

    @PutMapping(value="/updatePost/{post_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updatePost(@PathVariable int post_id, @RequestPart("data") PostDTO postDTO, @RequestPart(value = "file", required = false) MultipartFile file ) {
        return ResponseEntity.ok().body(postService.updatePost(post_id, postDTO, file));
    }

    @DeleteMapping("/deletePost/{post_id}")
    public ResponseEntity<ResponseDTO> deletePost(@PathVariable int post_id) {
        return ResponseEntity.ok().body(postService.deletePost(post_id));
    }
}
