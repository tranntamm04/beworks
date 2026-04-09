package com.example.repository;

import com.example.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("""
        SELECT w FROM Workspace w
        JOIN WorkspaceMember m ON m.workspace.id = w.id
        WHERE m.user.id = :userId
    """)
    List<Workspace> findByUserId(Long userId);
}