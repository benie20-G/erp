package com.erp.repositories;

import com.example.erp.entity.Employee;
import com.example.erp.entity.Employment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmploymentRepository extends JpaRepository<Employment, Long> {
    Optional<Employment> findByEmployeeAndStatus(Employee employee, Employment.Status status);
}