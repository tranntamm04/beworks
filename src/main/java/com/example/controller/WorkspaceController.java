package com.example.controller;

import com.example.mapper.WorkspaceMapper;
import com.example.service.CurrentUserService;
import com.example.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.dto.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService service;
    private final CurrentUserService security;

    @PostMapping
    public WorkspaceResponse create(@RequestBody CreateWorkspaceRequest req) {
        var ws = service.create(req.getName(), security.get().getId());
        return WorkspaceMapper.toResponse(ws);
    }

    @GetMapping("/my")
    public List<WorkspaceResponse> my() {
        return service.getMyWorkspaces(security.get().getId())
                .stream()
                .map(WorkspaceMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}/members")
    public List<MemberResponse> members(@PathVariable Long id) {
        return service.getMembers(id, security.get().getId())
                .stream()
                .map(WorkspaceMapper::toMember)
                .toList();
    }

    @PutMapping("/{id}")
    public WorkspaceResponse update(@PathVariable Long id, @RequestBody UpdateWorkspaceRequest req) {
        var ws = service.update(id, req.getName(), security.get().getId());
        return WorkspaceMapper.toResponse(ws);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id, security.get().getId());
    }

    @DeleteMapping("/{id}/members/{userId}")
    public void removeMember(@PathVariable Long id, @PathVariable Long userId) {
        service.removeMember(id, userId, security.get().getId());
    }

    @PutMapping("/{id}/members/{userId}/role")
    public void changeRole(@PathVariable Long id, @PathVariable Long userId, @RequestBody ChangeRoleRequest req) {
        service.changeRole(id, userId, req.getRole(), security.get().getId());
    }
}