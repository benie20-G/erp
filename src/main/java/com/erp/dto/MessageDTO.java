package com.erp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageDTO {
    private Long id;
    
    @NotNull
    private Long employeeId;
    
    @NotBlank
    private String message;
    
    @NotBlank
    private String monthYear;
}