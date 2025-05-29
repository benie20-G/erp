package com.erp.repositories;

import com.example.erp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    List<Employee> findByStatus(Employee.Status status);
    Optional<Employee> findByVerificationToken(String token);
    Optional<Employee> findByResetPasswordToken(String token);
}