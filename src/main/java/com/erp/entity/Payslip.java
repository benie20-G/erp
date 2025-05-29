package com.example.erp.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "payslip", uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "month", "year"}))
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @NotNull
    private Double houseAmount;

    @NotNull
    private Double transportAmount;

    @NotNull
    private Double employeeTaxedAmount;

    @NotNull
    private Double pensionAmount;

    @NotNull
    private Double medicalInsuranceAmount;

    @NotNull
    private Double otherTaxedAmount;

    @NotNull
    private Double grossSalary;

    @NotNull
    private Double netSalary;

    @NotNull
    private Integer month;

    @NotNull
    private Integer year;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, PAID
    }
}