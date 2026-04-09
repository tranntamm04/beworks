package com.example.dto;

import lombok.Data;

@Data
public class MoveTaskRequest {
    private Long columnId;
    private Integer position;
}