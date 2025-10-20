package com.endora.api.features.commend.service.impl;

import com.endora.api.features.commend.dto.CommentRequest;
import com.endora.api.features.commend.dto.CommentResponse;
import com.endora.api.features.commend.dto.CommentUpdateRequest;
import com.endora.api.features.commend.model.Comment;
import com.endora.api.features.commend.repository.CommentRepository;
import com.endora.api.features.commend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final int DAILY_COMMENT_LIMIT = 100;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentResponse createComment(CommentRequest request) {
        LocalDateTime startOfToday = LocalDateTime.now().with(LocalTime.MIN);
        long todayCount = commentRepository.countCommentsCreatedToday(startOfToday);

        if (todayCount >= DAILY_COMMENT_LIMIT) {
            throw new RuntimeException("Daily comment limit of " + DAILY_COMMENT_LIMIT + " exceeded");
        }

        Comment comment = Comment.builder()
                .content(request.content())
                .author(request.author())
                .isDefault(false)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return mapToResponse(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return mapToResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long id, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        if (request.content() != null) {
            comment.setContent(request.content());
        }
        if (request.author() != null) {
            comment.setAuthor(request.author());
        }

        Comment updatedComment = commentRepository.save(comment);
        return mapToResponse(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void resetDailyComments() {
        // Delete all non-default comments
        commentRepository.deleteNonDefaultComments();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTodayCommentCount() {
        LocalDateTime startOfToday = LocalDateTime.now().with(LocalTime.MIN);
        return commentRepository.countCommentsCreatedToday(startOfToday);
    }

    private CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor(),
                comment.getCreatedAt(),
                comment.getIsDefault()
        );
    }
}
