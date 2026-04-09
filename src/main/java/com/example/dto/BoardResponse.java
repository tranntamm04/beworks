package com.example.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
public class BoardResponse {
    private Long columnId;
    private String columnName;
    private List<TaskResponse> tasks;
}