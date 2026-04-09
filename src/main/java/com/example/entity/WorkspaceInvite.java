package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workspace_invites")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class WorkspaceInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private WorkspaceRole role;

    private boolean accepted;
}