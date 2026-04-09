package com.example.service;

import com.example.entity.Project;

import java.util.List;

public interface ProjectService {

    Project create(String name, Long workspaceId, Long userId);

    List<Project> getByWorkspace(Long workspaceId, Long userId);

    Project update(Long id, String name, Long userId);

    void delete(Long id, Long userId);
}