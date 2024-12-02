package com.example.eventix.controller;

import com.example.eventix.constant.Constant;
import com.example.eventix.dto.Event_SponsorDTO;
import com.example.eventix.dto.PostDTO;
import com.example.eventix.dto.ResponseDTO;
import com.example.eventix.service.PostService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/member")
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

    @PutMapping(value = "/updatePostStatus/{post_id}/{status}")
    public ResponseEntity<ResponseDTO> updatePostStatus(@PathVariable int post_id, @PathVariable String status){
        return ResponseEntity.ok().body(postService.updatePostStatus(post_id, status));
    }

    @GetMapping("/uploads/events/eventImages/{imageName}")
    public ResponseEntity<Resource> getEventImage(@PathVariable String imageName) {
        try {
            // Using the constant for the event photo directory
            Path imagePath = Paths.get(Constant.EVENT_PHOTO_DIRECTORY).resolve(imageName).normalize();
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Detect content type
                String contentType = Files.probeContentType(imagePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                        .body((Resource) resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
