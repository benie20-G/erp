package com.erp.service;

import com.erp.dto.PayslipDTO;
import com.erp.entity.*;
import com.erp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import java.util.List;
import java.util.stream.Collectors;
import com.erp.entity.Employee;

@Service
public class PayrollService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private DeductionsRepository deductionsRepository;

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void generatePayroll(int month, int year) {
        List<Employee> employees = employeeRepository.findByStatus(Employee.Status.ACTIVE);
        List<Deductions> deductions = deductionsRepository.findAll();

        for (Employee employee : employees) {
            Employment employment = employmentRepository.findByEmployeeAndStatus(employee, Employment.Status.ACTIVE)
                    .orElse(null);
            if (employment == null) continue;

            if (payslipRepository.existsByEmployeeAndMonthAndYear(employee, month, year)) {
                continue;
            }

            double baseSalary = employment.getBaseSalary();
            double houseAmount = baseSalary * getDeductionPercentage(deductions, "Housing");
            double transportAmount = baseSalary * getDeductionPercentage(deductions, "Transport");
            double grossSalary = baseSalary + houseAmount + transportAmount;
            double taxAmount = baseSalary * getDeductionPercentage(deductions, "Employee Tax");
            double pensionAmount = baseSalary * getDeductionPercentage(deductions, "Pension");
            double medicalAmount = baseSalary * getDeductionPercentage(deductions, "Medical Insurance");
            double otherAmount = baseSalary * getDeductionPercentage(deductions, "Others");
            double netSalary = grossSalary - (taxAmount + pensionAmount + medicalAmount + otherAmount);

            if (netSalary < 0) {
                throw new IllegalStateException("Deductions exceed gross salary for employee: " + employee.getCode());
            }

            Payslip payslip = new Payslip();
            payslip.setEmployee(employee);
            payslip.setHouseAmount(houseAmount);
            payslip.setTransportAmount(transportAmount);
            payslip.setEmployeeTaxedAmount(taxAmount);
            payslip.setPensionAmount(pensionAmount);
            payslip.setMedicalInsuranceAmount(medicalAmount);
            payslip.setOtherTaxedAmount(otherAmount);
            payslip.setGrossSalary(grossSalary);
            payslip.setNetSalary(netSalary);
            payslip.setMonth(month);
            payslip.setYear(year);
            payslip.setStatus(Payslip.Status.PENDING);

            payslipRepository.save(payslip);
        }
    }

    @Transactional
    public PayslipDTO approvePayslip(Long id) throws MessagingException {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found"));
        if (payslip.getStatus() == Payslip.Status.PAID) {
            throw new IllegalStateException("Payslip already approved");
        }
        payslip.setStatus(Payslip.Status.PAID);
        payslip = payslipRepository.save(payslip);

        // The database trigger will create the Message entity
        List<Message> messages = messageRepository.findByEmployee(payslip.getEmployee());
        Payslip finalPayslip = payslip;
        Message latestMessage = messages.stream()
                .filter(m -> m.getMonthYear().equals(finalPayslip.getMonth() + "/" + finalPayslip.getYear()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Message not found for payslip"));
        emailService.sendSalaryEmail(latestMessage);

        return mapEntityToDto(payslip);
    }

    public List<PayslipDTO> getPayslipsByEmployee(Long employeeId, int month, int year) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return payslipRepository.findByEmployeeAndMonthAndYear(employee, month, year).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<PayslipDTO> getPayslipsByMonthYear(int month, int year) {
        return payslipRepository.findByMonthAndYear(month, year).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private double getDeductionPercentage(List<Deductions> deductions, String name) {
        return deductions.stream()
                .filter(d -> d.getDeductionName().equalsIgnoreCase(name))
                .findFirst()
                .map(Deductions::getPercentage)
                .orElse(0.0) / 100.0;
    }

    private PayslipDTO mapEntityToDto(Payslip payslip) {
        PayslipDTO dto = new PayslipDTO();
        dto.setId(payslip.getId());
        dto.setEmployeeId(payslip.getEmployee().getId());
        dto.setHouseAmount(payslip.getHouseAmount());
        dto.setTransportAmount(payslip.getTransportAmount());
        dto.setEmployeeTaxedAmount(payslip.getEmployeeTaxedAmount());
        dto.setPensionAmount(payslip.getPensionAmount());
        dto.setMedicalInsuranceAmount(payslip.getMedicalInsuranceAmount());
        dto.setOtherTaxedAmount(payslip.getOtherTaxedAmount());
        dto.setGrossSalary(payslip.getGrossSalary());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setMonth(payslip.getMonth());
        dto.setYear(payslip.getYear());
        dto.setStatus(payslip.getStatus().name());
        return dto;
    }
}