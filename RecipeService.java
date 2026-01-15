package com.tastopia.app.service;

import com.tastopia.app.model.InternalRecipe;
import com.tastopia.app.repository.InternalRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    
    @Autowired
    private InternalRecipeRepository internalRepo;
    
    /**
     * Search by Title
     */
    public List<InternalRecipe> searchByTitle(String query) {
        return internalRepo.findByTitleContainingIgnoreCase(query);
    }
    
    public Page<InternalRecipe> searchByTitle(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return internalRepo.findByTitleContainingIgnoreCase(query, pageable);
    }
    
    /**
     * Search by Ingredients - ALL match (current logic)
     */
    public List<InternalRecipe> searchByIngredients(String ingredientInput) {
        String[] ingredients = ingredientInput.split(",");
        
        List<InternalRecipe> results = internalRepo
                .findByIngredientsContainingIgnoreCase(ingredients[0].trim());
        
        for (int i = 1; i < ingredients.length; i++) {
            String nextIngredient = ingredients[i].trim().toLowerCase();
            results = results.stream()
                .filter(recipe -> recipe.getIngredients() != null && 
                        recipe.getIngredients().toLowerCase().contains(nextIngredient))
                .collect(Collectors.toList());
        }
        
        return results;
    }
    
    /**
     * Search by Ingredients - ANY match
     */
    public List<InternalRecipe> searchByIngredientsAny(String ingredientInput) {
        String[] ingredients = ingredientInput.split(",");
        
        return Arrays.stream(ingredients)
                .flatMap(ing -> internalRepo
                        .findByIngredientsContainingIgnoreCase(ing.trim()).stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    /**
     * Flexible search with match type
     */
    public List<InternalRecipe> searchByIngredientsFlexible(String ingredientInput, String matchType) {
        if ("ANY".equalsIgnoreCase(matchType)) {
            return searchByIngredientsAny(ingredientInput);
        } else {
            return searchByIngredients(ingredientInput); // Default to ALL
        }
    }
    
    /**
     * Get all recipes with pagination
     */
    public Page<InternalRecipe> getAllRecipes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return internalRepo.findAll(pageable);
    }
    
    /**
     * Search recipes by any field
     */
    public Page<InternalRecipe> searchRecipes(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return internalRepo.searchRecipes(query, pageable);
    }
    
    /**
     * Get recipe by ID
     */
    public InternalRecipe getRecipeById(String id) {
        return internalRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));
    }
    
    /**
     * Filter by difficulty
     */
    public List<InternalRecipe> getRecipesByDifficulty(String difficulty) {
        return internalRepo.findByDifficulty(difficulty.toUpperCase());
    }
}