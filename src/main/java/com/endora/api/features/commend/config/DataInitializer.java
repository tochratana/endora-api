package com.endora.api.features.commend.config;

import com.endora.api.features.commend.model.Comment;
import com.endora.api.features.commend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component("commentDataInitializer")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CommentRepository commentRepository;

    @Override
    public void run(String... args) {
        if (commentRepository.findDefaultComments().isEmpty()) {
            initializeDefaultComments();
        }
    }

    private void initializeDefaultComments() {
        List<Comment> defaultComments = Arrays.asList(
                Comment.builder()
                        .content("Welcome to our comment system! This is the first default comment.")
                        .author("System")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("This is a sample comment to demonstrate the functionality.")
                        .author("Admin")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("You can create up to 100 comments per day!")
                        .author("System")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("Default comments will always remain after daily reset.")
                        .author("Admin")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("Feel free to edit and delete user-created comments.")
                        .author("System")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("This comment system supports full CRUD operations.")
                        .author("Admin")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("All comments are sorted by creation time, newest first.")
                        .author("System")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("The system automatically resets every day at midnight.")
                        .author("Admin")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("Thank you for using our comment API!")
                        .author("System")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build(),
                Comment.builder()
                        .content("Happy commenting! Remember to be respectful.")
                        .author("Admin")
                        .isDefault(true)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        commentRepository.saveAll(defaultComments);
    }
}