package com.example.service.impl;

import com.example.dto.InviteRequest;
import com.example.entity.*;
import com.example.repository.*;
import com.example.service.WorkspaceInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceInviteServiceImpl implements WorkspaceInviteService {

    private final WorkspaceInviteRepository repo;
    private final WorkspaceMemberRepository memberRepo;
    private final WorkspaceRepository workspaceRepo;

    @Override
    public WorkspaceInvite invite(Long wsId, InviteRequest req) {

        WorkspaceInvite invite = WorkspaceInvite.builder()
                .workspace(workspaceRepo.findById(wsId).orElseThrow())
                .userId(req.getUserId())
                .role(req.getRole())
                .accepted(false)
                .build();

        return repo.save(invite);
    }

    @Override
    public void accept(Long inviteId) {

        WorkspaceInvite inv = repo.findById(inviteId).orElseThrow();

        if (inv.isAccepted()) return;

        memberRepo.save(
                WorkspaceMember.builder()
                        .workspace(inv.getWorkspace())
                        .user(User.builder().id(inv.getUserId()).build())
                        .role(inv.getRole())
                        .build()
        );

        inv.setAccepted(true);
        repo.save(inv);
    }
}