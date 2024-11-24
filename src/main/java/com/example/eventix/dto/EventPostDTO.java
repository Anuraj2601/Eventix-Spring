package com.example.eventix.dto;


import com.example.eventix.entity.EventPost;
import com.example.eventix.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventPostDTO {

    private int e_post_id;
    private String title;
    private String description;
    private String post_image;
    private EventPost.eventPostType post_status;
    private LocalDateTime date_posted;
    private int club_id;
    private Integer published_user_id;
    private int event_id;
}
