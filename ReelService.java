package com.tastopia.app.service;

import com.tastopia.app.model.FoodReel;
import com.tastopia.app.repository.ReelRepository;
import com.tastopia.app.repository.UserprofileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReelService {
    
    @Autowired
    private ReelRepository reelRepository;
    
    @Autowired
    private UserprofileRepository UserprofileRepository;
    
    public FoodReel createReel(FoodReel reel) {
        FoodReel saved = reelRepository.save(reel);
        updateUserReelCount(reel.getUsername(), 1);
        return saved;
    }
    
    public Page<FoodReel> getAllReels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reelRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    public Page<FoodReel> getUserReels(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reelRepository.findByUsername(username, pageable);
    }
    
    public FoodReel likeReel(String reelId, String username) {
        FoodReel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new RuntimeException("Reel not found"));
        
        if (!reel.getLikedBy().contains(username)) {
            reel.getLikedBy().add(username);
            reel.setLikes(reel.getLikes() + 1);
        }
        
        return reelRepository.save(reel);
    }
    
    public FoodReel unlikeReel(String reelId, String username) {
        FoodReel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new RuntimeException("Reel not found"));
        
        if (reel.getLikedBy().contains(username)) {
            reel.getLikedBy().remove(username);
            reel.setLikes(Math.max(0, reel.getLikes() - 1));
        }
        
        return reelRepository.save(reel);
    }
    
    public FoodReel incrementViews(String reelId) {
        FoodReel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new RuntimeException("Reel not found"));
        
        reel.setViews(reel.getViews() + 1);
        return reelRepository.save(reel);
    }
    
    public void deleteReel(String reelId, String username) {
        FoodReel reel = reelRepository.findById(reelId)
                .orElseThrow(() -> new RuntimeException("Reel not found"));
        
        if (!reel.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this reel");
        }
        
        reelRepository.deleteById(reelId);
        updateUserReelCount(username, -1);
    }
    
    private void updateUserReelCount(String username, int delta) {
        UserprofileRepository.findByUsername(username).ifPresent(profile -> {
            profile.setReelsCount(Math.max(0, profile.getReelsCount() + delta));
            UserprofileRepository.save(profile);
        });
    }
}