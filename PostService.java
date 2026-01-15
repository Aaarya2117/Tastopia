package com.tastopia.app.service;

import com.tastopia.app.model.RecipePost;
import com.tastopia.app.repository.PostRepository;
import com.tastopia.app.repository.UserprofileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserprofileRepository userProfileRepository;
    
    public RecipePost createPost(RecipePost post) {
        RecipePost saved = postRepository.save(post);
        updateUserPostCount(post.getUsername(), 1);
        return saved;
    }
    
    public Page<RecipePost> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    public Page<RecipePost> getUserPosts(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByUsername(username, pageable);
    }
    
    public RecipePost likePost(String postId, String username) {
        RecipePost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.getLikedBy().contains(username)) {
            post.getLikedBy().add(username);
            post.setLikes(post.getLikes() + 1);
        }
        
        return postRepository.save(post);
    }
    
    public RecipePost unlikePost(String postId, String username) {
        RecipePost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (post.getLikedBy().contains(username)) {
            post.getLikedBy().remove(username);
            post.setLikes(Math.max(0, post.getLikes() - 1));
        }
        
        return postRepository.save(post);
    }
    
    public void deletePost(String postId, String username) {
        RecipePost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this post");
        }
        
        postRepository.deleteById(postId);
        updateUserPostCount(username, -1);
    }
    
    private void updateUserPostCount(String username, int delta) {
        userProfileRepository.findByUsername(username).ifPresent(profile -> {
            profile.setPostsCount(Math.max(0, profile.getPostsCount() + delta));
            userProfileRepository.save(profile);
        });
    }
}
