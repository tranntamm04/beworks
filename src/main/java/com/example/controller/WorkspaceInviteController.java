package com.example.controller;

import com.example.dto.InviteRequest;
import com.example.entity.WorkspaceInvite;
import com.example.service.WorkspaceInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceInviteController {

    private final WorkspaceInviteService service;

    @PostMapping("/{id}/invite")
    public WorkspaceInvite invite(@PathVariable Long id,
                                  @RequestBody InviteRequest req) {
        return service.invite(id, req);
    }

    @PostMapping("/invite/{id}/accept")
    public void accept(@PathVariable Long id) {
        service.accept(id);
    }
}