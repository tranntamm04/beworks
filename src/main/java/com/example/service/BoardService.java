package com.example.service;

import com.example.dto.BoardResponse;

import java.util.List;

public interface BoardService {
    List<BoardResponse> getBoard(Long projectId);
}
