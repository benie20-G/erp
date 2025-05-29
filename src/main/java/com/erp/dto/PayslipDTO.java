package com.erp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PayslipDTO {
    private Long id;
    
    @NotNull
    private Long employeeId;
    
    @NotNull
    private Double houseAmount;
    
    @NotNull
    private Double transportAmount;
    
    @NotNull
    private Double employeeTaxedAmount;
    
    @NotNull
    private Double pensionAmount;
    
    @NotNull
    private Double medicalInsuranceAmount;
    
    @NotNull
    private Double otherTaxedAmount;
    
    @NotNull
    private Double grossSalary;
    
    @NotNull
    private Double netSalary;
    
    @NotNull
    private Integer month;
    
    @NotNull
    private Integer year;
    
    @NotNull
    private String status;
}