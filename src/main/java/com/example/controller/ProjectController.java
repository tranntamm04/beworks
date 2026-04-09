package com.example.controller;

import com.example.entity.Project;
import com.example.service.CurrentUserService;
import com.example.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;
    private final CurrentUserService security;

    @PostMapping
    public Project create(@RequestBody Map<String, Object> req) {
        return service.create((String) req.get("name"), Long.valueOf(req.get("workspaceId").toString()), security.get().getId());
    }

    @GetMapping
    public List<Project> get(@RequestParam Long workspaceId) {
        return service.getByWorkspace(workspaceId, security.get().getId());
    }

    @PutMapping("/{id}")
    public Project update(@PathVariable Long id, @RequestBody Map<String, String> req) {
        return service.update(id, req.get("name"), security.get().getId()
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id, security.get().getId());
    }
}