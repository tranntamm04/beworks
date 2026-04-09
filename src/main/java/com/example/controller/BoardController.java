package com.example.controller;

import com.example.dto.BoardResponse;
import com.example.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    @GetMapping("/{projectId}")
    public List<BoardResponse> get(@PathVariable Long projectId) {
        return service.getBoard(projectId);
    }
}