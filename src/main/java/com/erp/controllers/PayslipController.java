package com.erp.controllers;

import com.erp.dto.PayslipDTO;
import com.erp.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;


import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payslip")
@SecurityRequirement(name = "bearerAuth")
public class PayslipController {
    @Autowired
    private PayrollService payrollService;

    @Operation(summary = "Generate payroll for a month and year", description = "Accessible by MANAGER")
    @PostMapping("/generate/{month}/{year}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> generatePayroll(@PathVariable int month, @PathVariable int year) {
        payrollService.generatePayroll(month, year);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Approve a payslip", description = "Accessible by ADMIN")
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PayslipDTO> approvePayslip(@PathVariable Long id) throws MessagingException {
        return ResponseEntity.ok(payrollService.approvePayslip(id));
    }

    @Operation(summary = "Get payslips by employee, month, and year", description = "Accessible by EMPLOYEE")
    @GetMapping("/employee/{employeeId}/{month}/{year}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<PayslipDTO>> getPayslipsByEmployee(@PathVariable Long employeeId, @PathVariable int month, @PathVariable int year) {
        return ResponseEntity.ok(payrollService.getPayslipsByEmployee(employeeId, month, year));
    }

    @Operation(summary = "Get all payslips for a month and year", description = "Accessible by MANAGER")
    @GetMapping("/{month}/{year}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<PayslipDTO>> getPayslipsByMonthYear(@PathVariable int month, @PathVariable int year) {
        return ResponseEntity.ok(payrollService.getPayslipsByMonthYear(month, year));
    }
}