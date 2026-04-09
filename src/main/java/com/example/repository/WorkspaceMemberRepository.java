package com.example.repository;

import com.example.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {

    Optional<WorkspaceMember> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    List<WorkspaceMember> findByWorkspaceId(Long workspaceId);
}