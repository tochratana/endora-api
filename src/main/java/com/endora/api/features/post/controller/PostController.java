package com.endora.api.features.post.controller;

import com.endora.api.features.post.dto.CreatePostDTO;
import com.endora.api.features.post.dto.PostResponseDTO;
import com.endora.api.features.post.dto.PostSummaryDTO;
import com.endora.api.features.post.dto.UpdatePostDTO;
import com.endora.api.features.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") // Allow CORS for frontend development
public class PostController {

    private final PostService postService;

    // Create a new post
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostDTO createPostDTO) {
        try {
            PostResponseDTO createdPost = postService.createPost(createPostDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get all posts (summary view)
    @GetMapping
    public ResponseEntity<Page<PostSummaryDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        // Parse sort parameters
        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] parts = sortOrder.split(",");
                String property = parts[0];
                Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction, property));
            }
        } else {
            orders.add(new Sort.Order(Sort.Direction.ASC, sort[0]));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<PostSummaryDTO> posts = postService.getAllPosts(pageable);

        return ResponseEntity.ok(posts);
    }

    // Get post by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostResponseDTO post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update post
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @Valid @RequestBody UpdatePostDTO updatePostDTO) {
        try {
            // Ensure ID in path matches ID in body
            if (!id.equals(updatePostDTO.id())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Path ID doesn't match body ID"));
            }

            PostResponseDTO updatedPost = postService.updatePost(updatePostDTO);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Delete post
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok().body(Map.of("message", "Post deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Search posts
    @GetMapping("/search")
    public ResponseEntity<List<PostSummaryDTO>> searchPosts(@RequestParam String query) {
        List<PostSummaryDTO> posts = postService.searchPosts(query);
        return ResponseEntity.ok(posts);
    }

    // Get posts by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<PostSummaryDTO>> getPostsByCategory(@PathVariable String category) {
        List<PostSummaryDTO> posts = postService.getPostsByCategory(category);
        return ResponseEntity.ok(posts);
    }

    // Get posts by author
    @GetMapping("/author/{author}")
    public ResponseEntity<List<PostSummaryDTO>> getPostsByAuthor(@PathVariable String author) {
        List<PostSummaryDTO> posts = postService.getPostsByAuthor(author);
        return ResponseEntity.ok(posts);
    }

    // Get posts by tag
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<PostSummaryDTO>> getPostsByTag(@PathVariable String tag) {
        List<PostSummaryDTO> posts = postService.getPostsByTag(tag);
        return ResponseEntity.ok(posts);
    }

    // Get API stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPosts", postService.getTotalPostCount());
        stats.put("userCreatedPosts", postService.getUserCreatedPostCount());
        stats.put("canCreateMore", postService.canCreatePost());
        stats.put("maxDailyPosts", 50);
        stats.put("remainingPosts", 50 - postService.getUserCreatedPostCount());
        return ResponseEntity.ok(stats);
    }

    // Admin endpoint to reset to default posts
    @PostMapping("/admin/reset")
    public ResponseEntity<Map<String, String>> resetToDefault() {
        postService.resetToDefaultPosts();
        return ResponseEntity.ok().body(Map.of("message", "Database reset to default posts"));
    }

    // Admin endpoint to initialize default posts
    @PostMapping("/admin/init-defaults")
    public ResponseEntity<Map<String, String>> initializeDefaults() {
        postService.initializeDefaultPosts();
        return ResponseEntity.ok().body(Map.of("message", "Default posts initialized"));
    }
}
