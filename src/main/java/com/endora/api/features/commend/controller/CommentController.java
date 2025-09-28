package com.endora.api.features.commend.controller;

import com.endora.api.features.commend.dto.CommentRequest;
import com.endora.api.features.commend.dto.CommentResponse;
import com.endora.api.features.commend.dto.CommentUpdateRequest;
import com.endora.api.features.commend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentRequest request) {
        CommentResponse response = commentService.createComment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        CommentResponse comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request) {
        CommentResponse updatedComment = commentService.updateComment(id, request);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetDailyComments() {
        commentService.resetDailyComments();
        return ResponseEntity.ok(Map.of("message", "Daily comments reset successfully"));
    }

    @GetMapping("/count/today")
    public ResponseEntity<Map<String, Object>> getTodayCommentCount() {
        long count = commentService.getTodayCommentCount();
        return ResponseEntity.ok(Map.of(
                "todayCount", count,
                "remainingToday", Math.max(0, 100 - count)
        ));
    }
}