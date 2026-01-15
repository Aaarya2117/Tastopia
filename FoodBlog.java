package com.tastopia.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "blogs")
@Data
public class FoodBlog {
    @Id
    private String id;
    
    private String username;
    private String title;
    
    @Field("content") 
    private String content;
    
    private String coverImageUrl;
    private List<String> tags = new ArrayList<>();
    
    private int likes = 0;
    private int views = 0;
    private int commentsCount = 0;
    private List<String> likedBy = new ArrayList<>();
    
    @CreatedDate
    private LocalDateTime createdAt;
}