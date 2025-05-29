package com.example.erp.service;

import com.example.erp.dto.EmploymentDTO;
import com.example.erp.entity.Employee;
import com.example.erp.entity.Employment;
import com.example.erp.repository.EmployeeRepository;
import com.example.erp.repository.EmploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmploymentService {
    @Autowired
    private EmploymentRepository employmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public EmploymentDTO createEmployment(EmploymentDTO dto) {
        Employment employment = new Employment();
        mapDtoToEntity(dto, employment);
        employment = employmentRepository.save(employment);
        return mapEntityToDto(employment);
    }

    public EmploymentDTO getEmployment(Long id) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found"));
        return mapEntityToDto(employment);
    }

    public List<EmploymentDTO> getAllEmployments() {
        return employmentRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmploymentDTO updateEmployment(Long id, EmploymentDTO dto) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found"));
        mapDtoToEntity(dto, employment);
        employment = employmentRepository.save(employment);
        return mapEntityToDto(employment);
    }

    @Transactional
    public void deleteEmployment(Long id) {
        if (!employmentRepository.existsById(id)) {
            throw new RuntimeException("Employment not found");
        }
        employmentRepository.deleteById(id);
    }

    private void mapDtoToEntity(EmploymentDTO dto, Employment employment) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employment.setCode(dto.getCode());
        employment.setEmployee(employee);
        employment.setDepartment(dto.getDepartment());
        employment.setPosition(dto.getPosition());
        employment.setBaseSalary(dto.getBaseSalary());
        employment.setStatus(Employment.Status.valueOf(dto.getStatus()));
        employment.setJoiningDate(dto.getJoiningDate());
    }

    private EmploymentDTO mapEntityToDto(Employment employment) {
        EmploymentDTO dto = new EmploymentDTO();
        dto.setId(employment.getId());
        dto.setCode(employment.getCode());
        dto.setEmployeeId(employment.getEmployee().getId());
        dto.setDepartment(employment.getDepartment());
        dto.setPosition(employment.getPosition());
        dto.setBaseSalary(employment.getBaseSalary());
        dto.setStatus(employment.getStatus().name());
        dto.setJoiningDate(employment.getJoiningDate());
        return dto;
    }
}