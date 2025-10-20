package com.endora.api.features.post.service.impl;

import com.endora.api.features.post.dto.CreatePostDTO;
import com.endora.api.features.post.dto.PostResponseDTO;
import com.endora.api.features.post.dto.PostSummaryDTO;
import com.endora.api.features.post.dto.UpdatePostDTO;
import com.endora.api.features.post.model.Post;
import com.endora.api.features.post.repository.PostRepository;
import com.endora.api.features.post.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private static final int MAX_DAILY_POSTS = 50;

    @PostConstruct
    public void init() {
        // Initialize default posts if none exist
        if (postRepository.findByIsDefaultTrue().isEmpty()) {
            initializeDefaultPosts();
        }
    }

    @Override
    @Transactional
    public PostResponseDTO createPost(CreatePostDTO createPostDTO) {
        if (!canCreatePost()) {
            throw new RuntimeException("Daily post limit reached. Maximum " + MAX_DAILY_POSTS + " posts per day allowed.");
        }

        Post post = new Post(
                createPostDTO.title(),
                createPostDTO.body(),
                createPostDTO.author(),
                createPostDTO.authorEmail(),
                createPostDTO.tags(),
                createPostDTO.category(),
                createPostDTO.imageUrl(),
                0, // initial views
                0, // initial likes
                false // not a default post
        );

        Post savedPost = postRepository.save(post);
        log.info("Created new post with ID: {}", savedPost.getId());
        return mapToResponseDTO(savedPost);
    }

    @Override
    public Page<PostSummaryDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(this::mapToSummaryDTO);
    }

    @Override
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        // Increment views
        post.setViews(post.getViews() + 1);
        post = postRepository.save(post);

        return mapToResponseDTO(post);
    }

    @Override
    @Transactional
    public PostResponseDTO updatePost(UpdatePostDTO updatePostDTO) {
        Post existingPost = postRepository.findById(updatePostDTO.id())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + updatePostDTO.id()));

        // Don't allow updating default posts
        if (existingPost.isDefault()) {
            throw new RuntimeException("Cannot update default posts");
        }

        // Update only provided fields
        if (updatePostDTO.title() != null) {
            existingPost.setTitle(updatePostDTO.title());
        }
        if (updatePostDTO.body() != null) {
            existingPost.setBody(updatePostDTO.body());
        }
        if (updatePostDTO.author() != null) {
            existingPost.setAuthor(updatePostDTO.author());
        }
        if (updatePostDTO.authorEmail() != null) {
            existingPost.setAuthorEmail(updatePostDTO.authorEmail());
        }
        if (updatePostDTO.tags() != null) {
            existingPost.setTags(updatePostDTO.tags());
        }
        if (updatePostDTO.category() != null) {
            existingPost.setCategory(updatePostDTO.category());
        }
        if (updatePostDTO.imageUrl() != null) {
            existingPost.setImageUrl(updatePostDTO.imageUrl());
        }

        Post savedPost = postRepository.save(existingPost);
        log.info("Updated post with ID: {}", savedPost.getId());
        return mapToResponseDTO(savedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        postRepository.delete(post);
        String postType = post.isDefault() ? "default post" : "post";
        log.info("Deleted {} with ID: {}", postType, id);
    }

    @Override
    public List<PostSummaryDTO> getPostsByCategory(String category) {
        return postRepository.findByCategoryIgnoreCase(category).stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostSummaryDTO> getPostsByAuthor(String author) {
        return postRepository.findByAuthorIgnoreCase(author).stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostSummaryDTO> getPostsByTag(String tag) {
        return postRepository.findByTagIgnoreCase(tag).stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostSummaryDTO> searchPosts(String query) {
        return postRepository.searchPosts(query).stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void resetToDefaultPosts() {
        postRepository.deleteAllNonDefaultPosts();
        log.info("Reset database to default posts only");
    }

    @Override
    public boolean canCreatePost() {
        return getUserCreatedPostCount() < MAX_DAILY_POSTS;
    }

    @Override
    public long getTotalPostCount() {
        return postRepository.count();
    }

    @Override
    public long getUserCreatedPostCount() {
        return postRepository.countByIsDefaultFalse();
    }

    @Override
    @Transactional
    public void initializeDefaultPosts() {
        List<Post> defaultPosts = Arrays.asList(
                new Post("Getting Started with Spring Boot",
                        "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can just run...",
                        "John Doe", "john.doe@example.com",
                        Arrays.asList("spring", "java", "backend"),
                        "Technology", "https://via.placeholder.com/400x200",
                        150, 25, true),

                new Post("Understanding REST APIs",
                        "REST (Representational State Transfer) is an architectural style for designing networked applications...",
                        "Jane Smith", "jane.smith@example.com",
                        Arrays.asList("rest", "api", "web"),
                        "Technology", "https://via.placeholder.com/400x200",
                        200, 35, true),

                new Post("Frontend Development Trends",
                        "The frontend development landscape is constantly evolving. Here are the latest trends...",
                        "Mike Johnson", "mike.johnson@example.com",
                        Arrays.asList("frontend", "javascript", "react"),
                        "Technology", "https://via.placeholder.com/400x200",
                        180, 30, true),

                new Post("Database Design Principles",
                        "Good database design is crucial for application performance and maintainability...",
                        "Sarah Wilson", "sarah.wilson@example.com",
                        Arrays.asList("database", "sql", "design"),
                        "Technology", "https://via.placeholder.com/400x200",
                        120, 20, true),

                new Post("DevOps Best Practices",
                        "DevOps is a set of practices that combines software development and IT operations...",
                        "David Brown", "david.brown@example.com",
                        Arrays.asList("devops", "ci/cd", "automation"),
                        "Technology", "https://via.placeholder.com/400x200",
                        160, 28, true),

                new Post("Mobile App Development",
                        "Mobile application development is the process of creating software applications that run on mobile devices...",
                        "Lisa Davis", "lisa.davis@example.com",
                        Arrays.asList("mobile", "android", "ios"),
                        "Technology", "https://via.placeholder.com/400x200",
                        140, 22, true),

                new Post("Cloud Computing Basics",
                        "Cloud computing is the delivery of computing services including servers, storage, databases...",
                        "Robert Taylor", "robert.taylor@example.com",
                        Arrays.asList("cloud", "aws", "azure"),
                        "Technology", "https://via.placeholder.com/400x200",
                        190, 32, true),

                new Post("Cybersecurity Fundamentals",
                        "Cybersecurity is the practice of protecting systems, networks, and programs from digital attacks...",
                        "Emily Chen", "emily.chen@example.com",
                        Arrays.asList("security", "cybersecurity", "privacy"),
                        "Technology", "https://via.placeholder.com/400x200",
                        170, 26, true),

                new Post("Machine Learning Introduction",
                        "Machine learning is a subset of artificial intelligence that provides systems the ability to learn...",
                        "Alex Martinez", "alex.martinez@example.com",
                        Arrays.asList("ml", "ai", "python"),
                        "Technology", "https://via.placeholder.com/400x200",
                        220, 40, true),

                new Post("Web Performance Optimization",
                        "Web performance optimization is about making your websites fast and providing a great user experience...",
                        "Chris Anderson", "chris.anderson@example.com",
                        Arrays.asList("performance", "web", "optimization"),
                        "Technology", "https://via.placeholder.com/400x200",
                        130, 24, true)
        );

        postRepository.saveAll(defaultPosts);
        log.info("Initialized {} default posts", defaultPosts.size());
    }

    private PostResponseDTO mapToResponseDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getAuthor(),
                post.getAuthorEmail(),
                post.getTags(),
                post.getCategory(),
                post.getImageUrl(),
                post.getViews(),
                post.getLikes(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.isDefault()
        );
    }

    private PostSummaryDTO mapToSummaryDTO(Post post) {
        return new PostSummaryDTO(
                post.getId(),
                post.getTitle(),
                post.getAuthor(),
                post.getCategory(),
                post.getTags(),
                post.getViews(),
                post.getLikes(),
                post.getCreatedAt(),
                post.isDefault()
        );
    }
}
