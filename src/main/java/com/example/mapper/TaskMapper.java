package com.example.mapper;

import com.example.dto.TaskResponse;
import com.example.entity.Task;

public class TaskMapper {

    public static TaskResponse toResponse(Task t) {
        return TaskResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .priority(t.getPriority() != null ? t.getPriority().name() : null)
                .columnId(t.getColumn().getId())
                .assigneeId(t.getAssignee() != null ? t.getAssignee().getId() : null)
                .build();
    }
}