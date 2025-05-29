package com.example.erp.controller;

import com.example.erp.dto.EmploymentDTO;
import com.example.erp.service.EmploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employment")
@SecurityRequirement(name = "bearerAuth")
public class EmploymentController {
    @Autowired
    private EmploymentService employmentService;

    @Operation(summary = "Create a new employment", description = "Accessible by ADMIN and MANAGER")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmploymentDTO> createEmployment(@Valid @RequestBody EmploymentDTO employmentDTO) {
        return ResponseEntity.ok(employmentService.createEmployment(employmentDTO));
    }

    @Operation(summary = "Get employment by ID", description = "Accessible by ADMIN, MANAGER, and EMPLOYEE")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<EmploymentDTO> getEmployment(@PathVariable Long id) {
        return ResponseEntity.ok(employmentService.getEmployment(id));
    }

    @Operation(summary = "Get all employments", description = "Accessible by ADMIN and MANAGER")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmploymentDTO>> getAllEmployments() {
        return ResponseEntity.ok(employmentService.getAllEmployments());
    }

    @Operation(summary = "Update employment by ID", description = "Accessible by ADMIN and MANAGER")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmploymentDTO> updateEmployment(@PathVariable Long id, @Valid @RequestBody EmploymentDTO employmentDTO) {
        return ResponseEntity.ok(employmentService.updateEmployment(id, employmentDTO));
    }

    @Operation(summary = "Delete employment by ID", description = "Accessible by ADMIN and MANAGER")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteEmployment(@PathVariable Long id) {
        employmentService.deleteEmployment(id);
        return ResponseEntity.ok().build();
    }
}