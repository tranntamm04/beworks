package com.example.mapper;

import com.example.entity.Workspace;
import com.example.entity.WorkspaceMember;
import com.example.dto.*;

public class WorkspaceMapper {

    public static WorkspaceResponse toResponse(Workspace ws) {
        return WorkspaceResponse.builder()
                .id(ws.getId())
                .name(ws.getName())
                .build();
    }

    public static MemberResponse toMember(WorkspaceMember m) {
        return MemberResponse.builder()
                .userId(m.getUser().getId())
                .role(m.getRole().name())
                .build();
    }
}