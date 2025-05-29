package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmploymentDTO {
    private Long id;
    
    @NotBlank
    private String code;
    
    @NotNull
    private Long employeeId;
    
    @NotBlank
    private String department;
    
    @NotBlank
    private String position;
    
    @NotNull
    private Double baseSalary;
    
    @NotBlank
    private String status;
    
    @NotNull
    private LocalDate joiningDate;
}