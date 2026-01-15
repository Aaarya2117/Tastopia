package com.tastopia.app.repository;

import com.tastopia.app.model.FoodBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BlogRepository extends MongoRepository<FoodBlog, String> {
    
    // Find by username
    List<FoodBlog> findByUsername(String username);
    Page<FoodBlog> findByUsername(String username, Pageable pageable);
    
    // Search by title
    Page<FoodBlog> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // Get popular blogs
    List<FoodBlog> findTop10ByOrderByLikesDesc();
    
    // Get recent blogs
    Page<FoodBlog> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // Find by tags
    List<FoodBlog> findByTagsContaining(String tag);
}