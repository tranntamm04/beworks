package com.example.service.impl;

import com.example.entity.*;
import com.example.exception.AppException;
import com.example.repository.*;
import com.example.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepo;
    private final WorkspaceMemberRepository memberRepo;
    private final UserRepository userRepo;

    @Override
    public Workspace create(String name, Long userId) {

        if (name == null || name.trim().isEmpty()) {
            throw new AppException("Workspace name required");
        }

        Workspace ws = workspaceRepo.save(
                Workspace.builder().name(name.trim()).build()
        );

        memberRepo.save(
                WorkspaceMember.builder()
                        .workspace(ws)
                        .user(userRepo.findById(userId).orElseThrow())
                        .role(WorkspaceRole.OWNER)
                        .build()
        );

        return ws;
    }

    @Override
    public List<Workspace> getMyWorkspaces(Long userId) {
        return workspaceRepo.findByUserId(userId);
    }

    @Override
    public List<WorkspaceMember> getMembers(Long workspaceId, Long userId) {

        memberRepo.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new AppException("Access denied"));

        return memberRepo.findByWorkspaceId(workspaceId);
    }

    @Override
    public Workspace update(Long id, String name, Long userId) {

        Workspace ws = workspaceRepo.findById(id)
                .orElseThrow(() -> new AppException("Workspace not found"));

        WorkspaceMember member = memberRepo
                .findByUserIdAndWorkspaceId(userId, id)
                .orElseThrow(() -> new AppException("Access denied"));

        if (member.getRole() != WorkspaceRole.OWNER) {
            throw new AppException("Only owner can update workspace");
        }

        ws.setName(name);
        return workspaceRepo.save(ws);
    }

    @Override
    public void delete(Long id, Long userId) {

        WorkspaceMember member = memberRepo
                .findByUserIdAndWorkspaceId(userId, id)
                .orElseThrow(() -> new AppException("Access denied"));

        if (member.getRole() != WorkspaceRole.OWNER) {
            throw new AppException("Only owner can delete workspace");
        }

        workspaceRepo.deleteById(id);
    }

    @Override
    public void removeMember(Long wsId, Long targetUserId, Long currentUserId) {

        WorkspaceMember current = memberRepo
                .findByUserIdAndWorkspaceId(currentUserId, wsId)
                .orElseThrow(() -> new AppException("Access denied"));

        if (current.getRole() != WorkspaceRole.OWNER) {
            throw new AppException("Only owner can remove");
        }

        WorkspaceMember target = memberRepo
                .findByUserIdAndWorkspaceId(targetUserId, wsId)
                .orElseThrow(() -> new AppException("Member not found"));

        memberRepo.delete(target);
    }

    @Override
    public void changeRole(Long wsId, Long targetUserId, String role, Long currentUserId) {

        WorkspaceMember current = memberRepo
                .findByUserIdAndWorkspaceId(currentUserId, wsId)
                .orElseThrow(() -> new AppException("Access denied"));

        if (current.getRole() != WorkspaceRole.OWNER) {
            throw new AppException("Only owner can change role");
        }

        WorkspaceMember target = memberRepo
                .findByUserIdAndWorkspaceId(targetUserId, wsId)
                .orElseThrow(() -> new AppException("Member not found"));

        target.setRole(WorkspaceRole.valueOf(role));
        memberRepo.save(target);
    }
}