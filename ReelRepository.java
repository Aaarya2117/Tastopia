package com.tastopia.app.repository;

import com.tastopia.app.model.FoodReel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReelRepository extends MongoRepository<FoodReel, String> {
    
    // Find by username
    List<FoodReel> findByUsername(String username);
    Page<FoodReel> findByUsername(String username, Pageable pageable);
    
    // Get trending reels (by views)
    List<FoodReel> findTop10ByOrderByViewsDesc();
    
    // Get recent reels
    Page<FoodReel> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    // Get popular reels (by likes)
    List<FoodReel> findTop10ByOrderByLikesDesc();
    
    // Find by tags
    List<FoodReel> findByTagsContaining(String tag);
}