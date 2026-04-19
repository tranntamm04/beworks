package com.example.service.impl;

import com.example.dto.column.*;
import com.example.entity.*;
import com.example.exception.AppException;
import com.example.repository.*;
import com.example.service.ColumnService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnServiceImpl implements ColumnService {

    private final BoardColumnRepository columnRepository;
    private final ProjectRepository projectRepository;
    private final WorkspaceMemberRepository memberRepository;

    @Override
    public ColumnResponse create(Long projectId, ColumnRequest request, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException("Project not found"));

        checkPermission(project, userId);

        int position = columnRepository.findByProjectIdOrderByPositionAsc(projectId).size();

        BoardColumn column = BoardColumn.builder()
                .name(request.getName())
                .project(project)
                .position(position)
                .build();

        columnRepository.save(column);

        return map(column);
    }

    @Override
    public List<ColumnResponse> getByProject(Long projectId, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException("Project not found"));

        checkMember(project, userId);

        return columnRepository.findByProjectIdOrderByPositionAsc(projectId)
                .stream().map(this::map).toList();
    }

    @Override
    public ColumnResponse update(Long columnId, ColumnRequest request, Long userId) {

        BoardColumn column = getColumn(columnId);
        checkPermission(column.getProject(), userId);

        column.setName(request.getName());

        return map(columnRepository.save(column));
    }

    @Override
    public void delete(Long columnId, Long userId) {

        BoardColumn column = getColumn(columnId);
        checkPermission(column.getProject(), userId);

        columnRepository.delete(column);
    }

    @Override
    public void reorder(Long columnId, Integer newPosition, Long userId) {

        BoardColumn column = getColumn(columnId);
        Project project = column.getProject();

        checkPermission(project, userId);

        List<BoardColumn> columns = columnRepository
                .findByProjectIdOrderByPositionAsc(project.getId());

        columns.remove(column);
        columns.add(newPosition, column);

        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setPosition(i);
        }

        columnRepository.saveAll(columns);
    }

    // ===== HELPER =====

    private BoardColumn getColumn(Long id) {
        return columnRepository.findById(id)
                .orElseThrow(() -> new AppException("Column not found"));
    }

    private void checkPermission(Project project, Long userId) {
        WorkspaceMember member = checkMember(project, userId);

        if (member.getRole() == WorkspaceRole.MEMBER) {
            throw new AppException("Permission denied");
        }
    }

    private WorkspaceMember checkMember(Project project, Long userId) {
        return memberRepository
                .findByUserIdAndWorkspaceId(userId, project.getWorkspace().getId())
                .orElseThrow(() -> new AppException("Access denied"));
    }

    private ColumnResponse map(BoardColumn c) {
        return ColumnResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .position(c.getPosition())
                .projectId(c.getProject().getId())
                .build();
    }
}