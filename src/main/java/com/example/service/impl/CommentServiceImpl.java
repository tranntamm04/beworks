package com.example.service.impl;

import com.example.entity.Comment;
import com.example.entity.Task;
import com.example.repository.CommentRepository;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import com.example.service.CommentService;
import com.example.service.PermissionService;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repo;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final PermissionService permission;

    @Override
    public Comment create(Long taskId, String content, Long userId) {

        Task task = taskRepo.findById(taskId).orElseThrow();

        permission.get(task.getProject().getWorkspace().getId(), userId);

        return repo.save(
                Comment.builder()
                        .content(content)
                        .task(task)
                        .user(userRepo.findById(userId).orElseThrow())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public List<Comment> getByTask(Long taskId, Long userId) {

        Task task = taskRepo.findById(taskId).orElseThrow();

        permission.get(task.getProject().getWorkspace().getId(), userId);

        return repo.findByTaskIdOrderByCreatedAtAsc(taskId);
    }
}