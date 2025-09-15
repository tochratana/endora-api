package com.endora.api.features.post.service;

import com.endora.api.features.post.dto.CreatePostDTO;
import com.endora.api.features.post.dto.PostResponseDTO;
import com.endora.api.features.post.dto.PostSummaryDTO;
import com.endora.api.features.post.dto.UpdatePostDTO;

import java.util.List;

public interface PostService {

    // CRUD Operations
    PostResponseDTO createPost(CreatePostDTO createPostDTO);

    List<PostSummaryDTO> getAllPosts();

    PostResponseDTO getPostById(Long id);

    PostResponseDTO updatePost(UpdatePostDTO updatePostDTO);

    void deletePost(Long id);

    // Additional operations
    List<PostSummaryDTO> getPostsByCategory(String category);

    List<PostSummaryDTO> getPostsByAuthor(String author);

    List<PostSummaryDTO> getPostsByTag(String tag);

    List<PostSummaryDTO> searchPosts(String query);

    // Daily reset and limits
    void resetToDefaultPosts();

    boolean canCreatePost();

    long getTotalPostCount();

    long getUserCreatedPostCount();

    // Initialize default posts
    void initializeDefaultPosts();
}
