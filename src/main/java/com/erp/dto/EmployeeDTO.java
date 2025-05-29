package com.erp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String roles;

    @NotBlank
    private String mobile;

    @NotNull
    private LocalDate dateOfBirth;

    @NotBlank
    private String status;

    private String verificationToken;

    @NotNull
    private Boolean isVerified;

    private String resetPasswordToken;
}