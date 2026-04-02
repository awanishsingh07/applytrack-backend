package com.awanish.applytrack_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String jobUrl;
    private String status;
    private String notes;
    private LocalDate applyDate;
    @ManyToOne
    private User user;
    @ManyToOne
    private Resume resume;
}