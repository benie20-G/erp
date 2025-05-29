package com.erp.repositories;

import com.example.erp.entity.Employee;
import com.example.erp.entity.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    boolean existsByEmployeeAndMonthAndYear(Employee employee, int month, int year);
    List<Payslip> findByEmployeeAndMonthAndYear(Employee employee, int month, int year);
    List<Payslip> findByMonthAndYear(int month, int year);
}