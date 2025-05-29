package com.erp.service;

import com.erp.dto.EmployeeDTO;
import com.erp.entity.Employee;
import com.erp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO dto) throws jakarta.mail.MessagingException {
        Employee employee = new Employee();
        mapDtoToEntity(dto, employee);
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setVerificationToken(UUID.randomUUID().toString());
        employee.setIsVerified(false);
        employee = employeeRepository.save(employee);
        emailService.sendVerificationEmail(employee);
        return mapEntityToDto(employee);
    }

    public EmployeeDTO getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return mapEntityToDto(employee);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        mapDtoToEntity(dto, employee);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        employee = employeeRepository.save(employee);
        return mapEntityToDto(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }

    @Transactional
    public void verifyEmployee(String token) {
        Employee employee = employeeRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));
        if (employee.getIsVerified()) {
            throw new RuntimeException("Employee already verified");
        }
        employee.setIsVerified(true);
        employee.setVerificationToken(null);
        employeeRepository.save(employee);
    }

    @Transactional
    public void requestPasswordReset(String email) throws jakarta.mail.MessagingException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setResetPasswordToken(UUID.randomUUID().toString());
        employeeRepository.save(employee);
        emailService.sendPasswordResetEmail(employee);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        Employee employee = employeeRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));
        employee.setPassword(passwordEncoder.encode(newPassword));
        employee.setResetPasswordToken(null);
        employeeRepository.save(employee);
    }

    private void mapDtoToEntity(EmployeeDTO dto, Employee employee) {
        employee.setCode(dto.getCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setRoles(dto.getRoles());
        employee.setMobile(dto.getMobile());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setStatus(Employee.Status.valueOf(dto.getStatus()));
        employee.setVerificationToken(dto.getVerificationToken());
        employee.setIsVerified(dto.getIsVerified());
        employee.setResetPasswordToken(dto.getResetPasswordToken());
    }

    private EmployeeDTO mapEntityToDto(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setCode(employee.getCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setRoles(employee.getRoles());
        dto.setMobile(employee.getMobile());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setStatus(employee.getStatus().name());
        dto.setVerificationToken(employee.getVerificationToken());
        dto.setIsVerified(employee.getIsVerified());
        dto.setResetPasswordToken(employee.getResetPasswordToken());
        return dto;
    }
}