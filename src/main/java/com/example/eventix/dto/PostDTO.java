package com.example.eventix.dto;

import com.example.eventix.entity.Post;
import com.example.eventix.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDTO {
    private int post_id;
    private String name;
    private String position;
    private String description;
    private String post_image;
    private Post.postType post_status;
    private LocalDateTime date_posted;
    private int club_id;
    private Integer published_user_id; //published user id
}

//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//public class PostDTO {
//    private int post_id;
//    private String name;
//    private String position;
//    private String description;
//    private String post_image;
//    private Post.postType post_status;
//    private LocalDateTime date_posted;
//    private int club_id;
//    private Users published_user; //published user id
//}
