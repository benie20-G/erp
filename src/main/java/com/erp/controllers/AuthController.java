package com.erp.controllers;

import com.erp.dto.LoginDTO;
import com.erp.service.AuthService;
import com.erp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        employeeService.verifyEmployee(token);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            employeeService.requestPasswordReset(email);
            return ResponseEntity.ok("Password reset link sent to email");
        } catch (MessagingException e) {
            logger.error("Failed to send password reset email for: {}", email, e);
            return ResponseEntity.status(500).body("Failed to send password reset email");
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody String newPassword) {
        employeeService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }
}