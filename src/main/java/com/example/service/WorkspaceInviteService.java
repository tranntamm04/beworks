package com.example.service;

import com.example.dto.InviteRequest;
import com.example.entity.WorkspaceInvite;

public interface WorkspaceInviteService {
    WorkspaceInvite invite(Long wsId, InviteRequest req);
    void accept(Long inviteId);
}
