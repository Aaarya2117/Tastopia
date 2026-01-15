package com.tastopia.app.repository;

import com.tastopia.app.model.InternalRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InternalRecipeRepository extends MongoRepository<InternalRecipe, String> {
    
    // Search by title
    List<InternalRecipe> findByTitleContainingIgnoreCase(String title);
    Page<InternalRecipe> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Search by ingredients
    List<InternalRecipe> findByIngredientsContainingIgnoreCase(String ingredients);
    
    // Search by difficulty
    List<InternalRecipe> findByDifficulty(String difficulty);
    
    // Combined search
    @Query("{ $or: [ { 'recipe_title': { $regex: ?0, $options: 'i' } }, { 'ingredients': { $regex: ?0, $options: 'i' } } ] }")
    Page<InternalRecipe> searchRecipes(String query, Pageable pageable);
}