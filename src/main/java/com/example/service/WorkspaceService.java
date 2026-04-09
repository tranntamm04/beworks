package com.example.service;

import com.example.entity.Workspace;
import com.example.entity.WorkspaceMember;

import java.util.List;

public interface WorkspaceService {

    Workspace create(String name, Long userId);

    List<Workspace> getMyWorkspaces(Long userId);

    List<WorkspaceMember> getMembers(Long workspaceId, Long userId);

    Workspace update(Long id, String name, Long userId);

    void delete(Long id, Long userId);

    void removeMember(Long wsId, Long targetUserId, Long currentUserId);

    void changeRole(Long wsId, Long targetUserId, String role, Long currentUserId);
}