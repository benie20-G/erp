package com.example.erp.controller;

import com.example.erp.dto.MessageDTO;
import com.example.erp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Operation(summary = "Get messages by employee", description = "Accessible by EMPLOYEE")
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<MessageDTO>> getMessagesByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(messageService.getMessagesByEmployee(employeeId));
    }
}