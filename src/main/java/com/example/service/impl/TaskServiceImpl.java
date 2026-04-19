package com.example.service.impl;

import com.example.dto.task.*;
import com.example.entity.*;
import com.example.exception.AppException;
import com.example.repository.*;
import com.example.service.TaskService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final BoardColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository memberRepository;

    @Override
    public TaskResponse create(Long columnId, TaskRequest request, Long userId) {

        BoardColumn column = getColumn(columnId);
        checkMember(column, userId);

        int position = taskRepository.findByColumnIdOrderByPositionAsc(columnId).size();

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .column(column)
                .project(column.getProject())
                .position(position)
                .status(TaskStatus.TODO)
                .createdBy(getUser(userId))
                .createdAt(LocalDateTime.now())
                .assignee(getAssignee(request.getAssigneeId()))
                .build();

        taskRepository.save(task);

        return map(task);
    }

    @Override
    public List<TaskResponse> getByColumn(Long columnId, Long userId) {

        BoardColumn column = getColumn(columnId);
        checkMember(column, userId);

        return taskRepository.findByColumnIdOrderByPositionAsc(columnId)
                .stream().map(this::map).toList();
    }

    @Override
    public TaskResponse update(Long taskId, TaskRequest request, Long userId) {

        Task task = getTask(taskId);
        checkMember(task.getColumn(), userId);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setAssignee(getAssignee(request.getAssigneeId()));

        return map(taskRepository.save(task));
    }

    @Override
    public void delete(Long taskId, Long userId) {

        Task task = getTask(taskId);
        checkMember(task.getColumn(), userId);

        taskRepository.delete(task);
    }

    @Override
    public TaskResponse move(Long taskId, TaskMoveRequest request, Long userId) {

        Task task = getTask(taskId);
        BoardColumn sourceColumn = task.getColumn();
        BoardColumn targetColumn = getColumn(request.getTargetColumnId());

        checkMember(sourceColumn, userId);
        checkMember(targetColumn, userId);

        List<Task> sourceTasks = taskRepository.findByColumnIdOrderByPositionAsc(sourceColumn.getId());
        List<Task> targetTasks = taskRepository.findByColumnIdOrderByPositionAsc(targetColumn.getId());

        sourceTasks.remove(task);

        if (!sourceColumn.getId().equals(targetColumn.getId())) {
            task.setColumn(targetColumn);
            targetTasks.add(request.getNewPosition(), task);
        } else {
            targetTasks = sourceTasks;
            targetTasks.add(request.getNewPosition(), task);
        }

        // reindex source
        for (int i = 0; i < sourceTasks.size(); i++) {
            sourceTasks.get(i).setPosition(i);
        }

        // reindex target
        for (int i = 0; i < targetTasks.size(); i++) {
            targetTasks.get(i).setPosition(i);
        }

        taskRepository.saveAll(sourceTasks);
        taskRepository.saveAll(targetTasks);

        return map(task);
    }

    // ===== HELPER =====

    private Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new AppException("Task not found"));
    }

    private BoardColumn getColumn(Long id) {
        return columnRepository.findById(id)
                .orElseThrow(() -> new AppException("Column not found"));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found"));
    }

    private User getAssignee(Long id) {
        if (id == null) return null;
        return getUser(id);
    }

    private void checkMember(BoardColumn column, Long userId) {
        boolean isMember = memberRepository.existsByUserIdAndWorkspaceId(
                userId,
                column.getProject().getWorkspace().getId()
        );

        if (!isMember) {
            throw new AppException("Access denied");
        }
    }

    private TaskResponse map(Task t) {
        return TaskResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .status(t.getStatus())
                .position(t.getPosition())
                .columnId(t.getColumn().getId())
                .assigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null)
                .dueDate(t.getDueDate())
                .build();
    }
}