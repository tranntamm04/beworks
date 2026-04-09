package com.example.service;

import com.example.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment create(Long taskId, String content, Long userId);

    List<Comment> getByTask(Long taskId, Long userId);
}