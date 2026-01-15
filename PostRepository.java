package com.tastopia.app.repository;

import com.tastopia.app.model.RecipePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<RecipePost, String> {
    
    // Find by username
    List<RecipePost> findByUsername(String username);
    Page<RecipePost> findByUsername(String username, Pageable pageable);
    
    // Get popular posts
    List<RecipePost> findTop10ByOrderByLikesDesc();
    
    // Get recent posts
    Page<RecipePost> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // Search by title
    Page<RecipePost> findByRecipeTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Find by tags
    List<RecipePost> findByTagsContaining(String tag);
}