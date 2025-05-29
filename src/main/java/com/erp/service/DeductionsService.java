package com.erp.services;

import com.example.erp.dto.DeductionsDTO;
import com.example.erp.entity.Deductions;
import com.example.erp.repository.DeductionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeductionsService {
    @Autowired
    private DeductionsRepository deductionsRepository;

    @Transactional
    public DeductionsDTO createDeductions(DeductionsDTO dto) {
        Deductions deductions = new Deductions();
        mapDtoToEntity(dto, deductions);
        deductions = deductionsRepository.save(deductions);
        return mapEntityToDto(deductions);
    }

    public DeductionsDTO getDeductions(Long id) {
        Deductions deductions = deductionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deductions not found"));
        return mapEntityToDto(deductions);
    }

    public List<DeductionsDTO> getAllDeductions() {
        return deductionsRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeductionsDTO updateDeductions(Long id, DeductionsDTO dto) {
        Deductions deductions = deductionsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deductions not found"));
        mapDtoToEntity(dto, deductions);
        deductions = deductionsRepository.save(deductions);
        return mapEntityToDto(deductions);
    }

    @Transactional
    public void deleteDeductions(Long id) {
        if (!deductionsRepository.existsById(id)) {
            throw new RuntimeException("Deductions not found");
        }
        deductionsRepository.deleteById(id);
    }

    private void mapDtoToEntity(DeductionsDTO dto, Deductions deductions) {
        deductions.setCode(dto.getCode());
        deductions.setDeductionName(dto.getDeductionName());
        deductions.setPercentage(dto.getPercentage());
    }

    private DeductionsDTO mapEntityToDto(Deductions deductions) {
        DeductionsDTO dto = new DeductionsDTO();
        dto.setId(deductions.getId());
        dto.setCode(deductions.getCode());
        dto.setDeductionName(deductions.getDeductionName());
        dto.setPercentage(deductions.getPercentage());
        return dto;
    }
}