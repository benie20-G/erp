package com.erp.service;

import com.erp.dto.LoginDTO;
import com.erp.entity.Employee;
import com.erp.repository.EmployeeRepository;
import com.erp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String login(LoginDTO loginDTO) {
        Employee employee = employeeRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if (!employee.getIsVerified()) {
            throw new RuntimeException("Email not verified");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        return jwtTokenProvider.generateToken(loginDTO.getEmail(), employee.getRoles());
    }
}