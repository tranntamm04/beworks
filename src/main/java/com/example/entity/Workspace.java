package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workspaces")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}