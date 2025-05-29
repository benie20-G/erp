package com.example.erp.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "employment")
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String code;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @NotBlank
    private String department;

    @NotBlank
    private String position;

    @NotNull
    private Double baseSalary;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private LocalDate joiningDate;

    public enum Status {
        ACTIVE, INACTIVE
    }
}