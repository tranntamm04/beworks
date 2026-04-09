package com.example.dto;

import lombok.*;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private Long columnId;
    private Long assigneeId;
}