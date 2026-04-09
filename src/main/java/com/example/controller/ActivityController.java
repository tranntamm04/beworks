package com.example.controller;

import com.example.entity.Activity;
import com.example.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/tasks/{taskId}/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityRepository repo;

    @GetMapping
    public List<Activity> get(@PathVariable Long taskId) {
        return repo.findByTaskIdOrderByCreatedAtDesc(taskId);
    }
}