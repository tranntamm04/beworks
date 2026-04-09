package com.example.service.impl;

import com.example.dto.BoardResponse;
import com.example.entity.*;
import com.example.mapper.TaskMapper;
import com.example.repository.*;
import com.example.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardColumnRepository columnRepo;
    private final TaskRepository taskRepo;

    @Override
    public List<BoardResponse> getBoard(Long projectId) {

        List<BoardColumn> columns =
                columnRepo.findByProjectIdOrderByPositionAsc(projectId);

        List<Task> tasks = taskRepo.findByProjectId(projectId);

        Map<Long, List<Task>> grouped =
                tasks.stream().collect(Collectors.groupingBy(t -> t.getColumn().getId()));

        return columns.stream().map(col ->
                BoardResponse.builder()
                        .columnId(col.getId())
                        .columnName(col.getName())
                        .tasks(
                                grouped.getOrDefault(col.getId(), List.of())
                                        .stream()
                                        .map(TaskMapper::toResponse)
                                        .toList()
                        )
                        .build()
        ).toList();
    }
}