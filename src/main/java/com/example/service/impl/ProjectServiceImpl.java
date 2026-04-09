package com.example.service.impl;

import com.example.entity.*;
import com.example.repository.*;
import com.example.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepo;
    private final WorkspaceRepository workspaceRepo;
    private final PermissionService permission;

    @Override
    public Project create(String name, Long workspaceId, Long userId) {

        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Project name required");
        }

        Workspace ws = workspaceRepo.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        permission.checkAdmin(workspaceId, userId);

        return projectRepo.save(
                Project.builder()
                        .name(name.trim())
                        .workspace(ws)
                        .status(ProjectStatus.ACTIVE)
                        .build()
        );
    }

    @Override
    public List<Project> getByWorkspace(Long workspaceId, Long userId) {

        permission.get(workspaceId, userId);

        return projectRepo.findByWorkspaceId(workspaceId);
    }

    @Override
    public Project update(Long id, String name, Long userId) {

        Project p = projectRepo.findById(id).orElseThrow();

        permission.checkAdmin(p.getWorkspace().getId(), userId);

        p.setName(name);

        return projectRepo.save(p);
    }

    @Override
    public void delete(Long id, Long userId) {

        Project p = projectRepo.findById(id).orElseThrow();

        permission.checkAdmin(p.getWorkspace().getId(), userId);

        projectRepo.delete(p);
    }
}