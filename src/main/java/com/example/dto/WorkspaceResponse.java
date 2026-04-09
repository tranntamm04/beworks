package com.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkspaceResponse {
    private Long id;
    private String name;
}