package com.tastopia.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "kaggle_recipes")
@Data
public class InternalRecipe {
    @Id
    private String id;
    
    @Field("recipe_title")
    private String title;
    
    @Field("ingredients")
    private String ingredients;
    
    private String instructions;
    private String imageUrl;
    private String cookingTime;
    private String difficulty; // EASY, MEDIUM, HARD
    private Integer servings;
    
    @Transient
    public List<String> getIngredientList() {
        if (ingredients == null || ingredients.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(ingredients.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}