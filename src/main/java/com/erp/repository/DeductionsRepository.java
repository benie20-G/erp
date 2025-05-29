package com.erp.repositories;

import com.example.erp.entity.Deductions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeductionsRepository extends JpaRepository<Deductions, Long> {
    Optional<Deductions> findByDeductionName(String deductionName);
}