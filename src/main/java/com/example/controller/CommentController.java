package com.example.controller;

import com.example.dto.CreateCommentRequest;
import com.example.entity.Comment;
import com.example.service.CommentService;
import com.example.service.CurrentUserService;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import lombok.*;

import java.util.List;

@Controller("*")
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;
    private final CurrentUserService security;

    @PostMapping
    public Comment create(@PathVariable Long taskId, @RequestBody CreateCommentRequest req) {
        return service.create(taskId, req.getContent(), security.get().getId());
    }

    @GetMapping
    public List<Comment> get(@PathVariable Long taskId) {
        return service.getByTask(taskId, security.get().getId());
    }
}