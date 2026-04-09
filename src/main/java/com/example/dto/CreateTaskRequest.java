package com.example.dto;

import lombok.Data;

@Data
public class CreateTaskRequest {
    private String title;
    private Long projectId;
    private Long columnId;
}