package com.example.service;

import com.example.entity.Task;
import com.example.entity.TaskPriority;
import java.util.List;

public interface TaskService {

    Task create(String title, Long projectId, Long columnId, Long userId);

    Task move(Long taskId, Long columnId, int position, Long userId);

    Task assign(Long taskId, Long assigneeId, Long userId);

    Task update(Long taskId, String title, String description,
                TaskPriority priority, Long userId);

    List<Task> getByProject(Long projectId, Long userId);
}