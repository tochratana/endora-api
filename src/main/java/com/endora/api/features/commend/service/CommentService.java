package com.endora.api.features.commend.service;

import com.endora.api.features.commend.dto.CommentRequest;
import com.endora.api.features.commend.dto.CommentResponse;
import com.endora.api.features.commend.dto.CommentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(CommentRequest request);

    Page<CommentResponse> getAllComments(Pageable pageable);

    CommentResponse getCommentById(Long id);

    CommentResponse updateComment(Long id, CommentUpdateRequest request);

    void deleteComment(Long id);

    void resetDailyComments();

    long getTodayCommentCount();
}