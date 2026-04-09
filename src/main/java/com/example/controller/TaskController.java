package com.example.controller;

import com.example.dto.*;
import com.example.entity.*;
import com.example.mapper.TaskMapper;
import com.example.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final CurrentUserService security;

    @PostMapping
    public TaskResponse create(@RequestBody CreateTaskRequest req) {

        return TaskMapper.toResponse(
                service.create(
                        req.getTitle(),
                        req.getProjectId(),
                        req.getColumnId(),
                        security.get().getId()
                )
        );
    }

    @PutMapping("/{id}/move")
    public TaskResponse move(@PathVariable Long id, @RequestBody MoveTaskRequest req) {
        return TaskMapper.toResponse(
                service.move(
                        id,
                        req.getColumnId(),
                        req.getPosition(),
                        security.get().getId()
                )
        );
    }

    @PutMapping("/{id}/assign")
    public TaskResponse assign(@PathVariable Long id, @RequestParam Long userId) {
        return TaskMapper.toResponse(service.assign(id, userId, security.get().getId()));
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @RequestBody Map<String, Object> req) {

        return TaskMapper.toResponse(
                service.update(id,
                        (String) req.get("title"),
                        (String) req.get("description"),
                        req.get("priority") != null
                                ? TaskPriority.valueOf(req.get("priority").toString())
                                : null,
                        security.get().getId()
                )
        );
    }

    @GetMapping
    public List<TaskResponse> get(@RequestParam Long projectId) {

        return service.getByProject(
                projectId,
                security.get().getId()
        ).stream().map(TaskMapper::toResponse).toList();
    }
}