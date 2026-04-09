package com.example.service.impl;

import com.example.entity.*;
import com.example.repository.*;
import com.example.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;
    private final BoardColumnRepository columnRepo;
    private final UserRepository userRepo;
    private final PermissionService permission;
    private final ActivityRepository activityRepo;

    @Override
    public Task create(String title, Long projectId, Long columnId, Long userId) {

        if (title == null || title.trim().isEmpty()) {
            throw new RuntimeException("Title required");
        }

        Project project = projectRepo.findById(projectId).orElseThrow();

        permission.get(project.getWorkspace().getId(), userId);

        BoardColumn col = columnRepo.findById(columnId).orElseThrow();

        Task task = Task.builder()
                .title(title)
                .project(project)
                .column(col)
                .createdBy(userRepo.findById(userId).orElseThrow())
                .createdAt(LocalDateTime.now())
                .position(0)
                .build();

        taskRepo.save(task);

        log(task, userId, "CREATE", "created task");

        return task;
    }

    @Override
    public Task move(Long taskId, Long columnId, int position, Long userId) {

        Task task = taskRepo.findById(taskId).orElseThrow();

        permission.get(task.getProject().getWorkspace().getId(), userId);

        BoardColumn col = columnRepo.findById(columnId).orElseThrow();

        task.setColumn(col);
        task.setPosition(position);

        log(task, userId, "MOVE", "moved task");

        return taskRepo.save(task);
    }

    @Override
    public Task assign(Long taskId, Long assigneeId, Long userId) {

        Task task = taskRepo.findById(taskId).orElseThrow();

        permission.checkAdmin(task.getProject().getWorkspace().getId(), userId);

        User user = userRepo.findById(assigneeId).orElseThrow();

        task.setAssignee(user);

        log(task, userId, "ASSIGN", "assigned task");

        return taskRepo.save(task);
    }

    @Override
    public Task update(Long taskId, String title, String description,
                       TaskPriority priority, Long userId) {

        Task task = taskRepo.findById(taskId).orElseThrow();

        permission.get(task.getProject().getWorkspace().getId(), userId);

        if (title != null) task.setTitle(title);
        if (description != null) task.setDescription(description);
        if (priority != null) task.setPriority(priority);

        log(task, userId, "UPDATE", "updated task");

        return taskRepo.save(task);
    }

    @Override
    public List<Task> getByProject(Long projectId, Long userId) {

        Project p = projectRepo.findById(projectId).orElseThrow();

        permission.get(p.getWorkspace().getId(), userId);

        return taskRepo.findByProjectId(projectId);
    }

    private void log(Task task, Long userId, String type, String msg) {

        activityRepo.save(
                Activity.builder()
                        .task(task)
                        .user(userRepo.findById(userId).orElseThrow())
                        .type(type)
                        .message(msg)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}