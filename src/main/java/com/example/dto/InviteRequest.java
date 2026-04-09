package com.example.dto;

import com.example.entity.WorkspaceRole;
import lombok.Data;

@Data
public class InviteRequest {
    private Long userId;
    private WorkspaceRole role;
}