package com.tastopia.app.controller;

import com.tastopia.app.model.*;
import com.tastopia.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hub")
@CrossOrigin(origins = "*")
public class TastopiaHubController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private ReelService reelService;
    
    @Autowired
    private BlogService blogService;
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;
    
    // ==================== RECIPE ENDPOINTS ====================
    
    @GetMapping("/recipes/search")
    public ResponseEntity<Page<InternalRecipe>> searchRecipes(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(recipeService.searchByTitle(query, page, size));
    }
    
    @GetMapping("/recipes/by-ingredients")
    public ResponseEntity<List<InternalRecipe>> searchByIngredients(
            @RequestParam String ingredients,
            @RequestParam(defaultValue = "ALL") String matchType) {
        return ResponseEntity.ok(
            recipeService.searchByIngredientsFlexible(ingredients, matchType)
        );
    }
    
    @GetMapping("/recipes/all")
    public ResponseEntity<Page<InternalRecipe>> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(recipeService.getAllRecipes(page, size));
    }
    
    @GetMapping("/recipes/{id}")
    public ResponseEntity<InternalRecipe> getRecipeById(@PathVariable String id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }
    
    @GetMapping("/recipes/difficulty/{difficulty}")
    public ResponseEntity<List<InternalRecipe>> getRecipesByDifficulty(
            @PathVariable String difficulty) {
        return ResponseEntity.ok(recipeService.getRecipesByDifficulty(difficulty));
    }
    
    // ==================== POST ENDPOINTS ====================
    
    @PostMapping("/posts/share")
    public ResponseEntity<RecipePost> sharePost(@RequestBody RecipePost post) {
        if (post.getRecipeTitle() == null || post.getRecipeTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(postService.createPost(post));
    }
    
    @GetMapping("/posts/all")
    public ResponseEntity<Page<RecipePost>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAllPosts(page, size));
    }
    
    @GetMapping("/posts/user/{username}")
    public ResponseEntity<Page<RecipePost>> getUserPosts(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getUserPosts(username, page, size));
    }
    
    @PostMapping("/posts/{id}/like")
    public ResponseEntity<RecipePost> likePost(
            @PathVariable String id,
            @RequestParam String username) {
        return ResponseEntity.ok(postService.likePost(id, username));
    }
    
    @PostMapping("/posts/{id}/unlike")
    public ResponseEntity<RecipePost> unlikePost(
            @PathVariable String id,
            @RequestParam String username) {
        return ResponseEntity.ok(postService.unlikePost(id, username));
    }
    
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Map<String, String>> deletePost(
            @PathVariable String id,
            @RequestParam String username) {
        postService.deletePost(id, username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Post deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    // ==================== REEL ENDPOINTS ====================
    
    @PostMapping("/reels/upload")
    public ResponseEntity<FoodReel> uploadReel(@RequestBody FoodReel reel) {
        if (reel.getVideoUrl() == null || reel.getVideoUrl().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reelService.createReel(reel));
    }
    
    @GetMapping("/reels/all")
    public ResponseEntity<Page<FoodReel>> getAllReels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reelService.getAllReels(page, size));
    }
    
    @GetMapping("/reels/user/{username}")
    public ResponseEntity<Page<FoodReel>> getUserReels(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reelService.getUserReels(username, page, size));
    }
    
    @PostMapping("/reels/{id}/like")
    public ResponseEntity<FoodReel> likeReel(
            @PathVariable String id,
            @RequestParam String username) {
        return ResponseEntity.ok(reelService.likeReel(id, username));
    }
    
    @PostMapping("/reels/{id}/unlike")
    public ResponseEntity<FoodReel> unlikeReel(
            @PathVariable String id,
            @RequestParam String username) {
        return ResponseEntity.ok(reelService.unlikeReel(id, username));
    }
    
    @PostMapping("/reels/{id}/view")
    public ResponseEntity<FoodReel> incrementReelViews(@PathVariable String id) {
        return ResponseEntity.ok(reelService.incrementViews(id));
    }
    
    @DeleteMapping("/reels/{id}")
    public ResponseEntity<Map<String, String>> deleteReel(
            @PathVariable String id,
            @RequestParam String username) {
        reelService.deleteReel(id, username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Reel deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    // ==================== BLOG ENDPOINTS ====================
    
    @PostMapping("/blogs/publish")
    public ResponseEntity<FoodBlog> publishBlog(@RequestBody FoodBlog blog) {
        if (blog.getTitle() == null || blog.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(blogService.createBlog(blog));
    }
    
    @GetMapping("/blogs/all")
    public ResponseEntity<Page<FoodBlog>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(blogService.getAllBlogs(page, size));
    }
    
    @GetMapping("/blogs/user/{username}")
    public ResponseEntity<Page<FoodBlog>> getUserBlogs(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(blogService.getUserBlogs(username, page, size));
    }
    
    @PostMapping("/blogs/{id}/like")
    public ResponseEntity<FoodBlog> likeBlog(
            @PathVariable String id,
            @RequestParam String username) {
        return ResponseEntity.ok(blogService.likeBlog(id, username));
    }
    
    @PostMapping("/blogs/{id}/unlike")
    public ResponseEntity<FoodBlog> unlikeBlog(
            @PathVariable String id,
            @RequestParam String username) {
        return ResponseEntity.ok(blogService.unlikeBlog(id, username));
    }
    
    @PostMapping("/blogs/{id}/view")
    public ResponseEntity<FoodBlog> incrementBlogViews(@PathVariable String id) {
        return ResponseEntity.ok(blogService.incrementViews(id));
    }
    
    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<Map<String, String>> deleteBlog(
            @PathVariable String id,
            @RequestParam String username) {
        blogService.deleteBlog(id, username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Blog deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    // ==================== COMMENT ENDPOINTS ====================
    
    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        if (comment.getText() == null || comment.getText().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(commentService.createComment(comment));
    }
    
    @GetMapping("/comments")
    public ResponseEntity<Page<Comment>> getComments(
            @RequestParam String contentId,
            @RequestParam String contentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
            commentService.getComments(contentId, contentType, page, size)
        );
    }
    
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable String id,
            @RequestParam String username) {
        commentService.deleteComment(id, username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comment deleted successfully");
        return ResponseEntity.ok(response);
    }
    
    // ==================== USER PROFILE ENDPOINTS ====================
    
    @PostMapping("/users/profile")
    public ResponseEntity<UserProfile> createOrUpdateProfile(
            @RequestBody UserProfile profile) {
        return ResponseEntity.ok(userService.createOrUpdateProfile(profile));
    }
    
    @GetMapping("/users/{username}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getProfile(username));
    }
    
    @PostMapping("/users/follow")
    public ResponseEntity<Map<String, String>> followUser(
            @RequestParam String follower,
            @RequestParam String following) {
        userService.followUser(follower, following);
        Map<String, String> response = new HashMap<>();
        response.put("message", follower + " is now following " + following);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/users/unfollow")
    public ResponseEntity<Map<String, String>> unfollowUser(
            @RequestParam String follower,
            @RequestParam String following) {
        userService.unfollowUser(follower, following);
        Map<String, String> response = new HashMap<>();
        response.put("message", follower + " unfollowed " + following);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users/{username}/followers")
    public ResponseEntity<List<String>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowers(username));
    }
    
    @GetMapping("/users/{username}/following")
    public ResponseEntity<List<String>> getFollowing(@PathVariable String username) {
        return ResponseEntity.ok(userService.getFollowing(username));
    }
    
    @GetMapping("/users/check-following")
    public ResponseEntity<Map<String, Boolean>> checkFollowing(
            @RequestParam String follower,
            @RequestParam String following) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFollowing", userService.isFollowing(follower, following));
        return ResponseEntity.ok(response);
    }
}