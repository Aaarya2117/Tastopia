package com.tastopia.app.service;

import com.tastopia.app.model.FoodBlog;
import com.tastopia.app.repository.BlogRepository;
import com.tastopia.app.repository.UserprofileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BlogService {
    
    @Autowired
    private BlogRepository blogRepository;
    
    @Autowired
    private UserprofileRepository userProfileRepository;
    
    public FoodBlog createBlog(FoodBlog blog) {
        FoodBlog saved = blogRepository.save(blog);
        updateUserBlogCount(blog.getUsername(), 1);
        return saved;
    }
    
    public Page<FoodBlog> getAllBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return blogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    public Page<FoodBlog> getUserBlogs(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return blogRepository.findByUsername(username, pageable);
    }
    
    public FoodBlog likeBlog(String blogId, String username) {
        FoodBlog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        
        if (!blog.getLikedBy().contains(username)) {
            blog.getLikedBy().add(username);
            blog.setLikes(blog.getLikes() + 1);
        }
        
        return blogRepository.save(blog);
    }
    
    public FoodBlog unlikeBlog(String blogId, String username) {
        FoodBlog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        
        if (blog.getLikedBy().contains(username)) {
            blog.getLikedBy().remove(username);
            blog.setLikes(Math.max(0, blog.getLikes() - 1));
        }
        
        return blogRepository.save(blog);
    }
    
    public FoodBlog incrementViews(String blogId) {
        FoodBlog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        
        blog.setViews(blog.getViews() + 1);
        return blogRepository.save(blog);
    }
    
    public void deleteBlog(String blogId, String username) {
        FoodBlog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        
        if (!blog.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this blog");
        }
        
        blogRepository.deleteById(blogId);
        updateUserBlogCount(username, -1);
    }
    
    private void updateUserBlogCount(String username, int delta) {
        userProfileRepository.findByUsername(username).ifPresent(profile -> {
            profile.setBlogsCount(Math.max(0, profile.getBlogsCount() + delta));
            userProfileRepository.save(profile);
        });
    }
}
