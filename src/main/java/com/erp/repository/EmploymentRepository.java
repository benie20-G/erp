package com.erp.repository;

import com.erp.entity.Employee;
import com.erp.entity.Employment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmploymentRepository extends JpaRepository<Employment, Long> {
    Optional<Employment> findByEmployeeAndStatus(Employee employee, Employment.Status status);
}