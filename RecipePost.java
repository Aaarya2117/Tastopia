package com.tastopia.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "posts")
@Data
public class RecipePost {
    @Id
    private String id;
    
    private String username;
    private String recipeTitle;
    private String description;
    private String imageUrl;
    private List<String> tags = new ArrayList<>();
    
    private int likes = 0;
    private int commentsCount = 0;
    private List<String> likedBy = new ArrayList<>(); // Track who liked
    
    @CreatedDate
    private LocalDateTime createdAt;
}