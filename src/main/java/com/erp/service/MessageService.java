package com.erp.services;

import com.example.erp.dto.MessageDTO;
import com.example.erp.entity.Employee;
import com.example.erp.entity.Message;
import com.example.erp.repository.EmployeeRepository;
import com.example.erp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<MessageDTO> getMessagesByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return messageRepository.findByEmployee(employee).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private MessageDTO mapEntityToDto(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setEmployeeId(message.getEmployee().getId());
        dto.setMessage(message.getMessage());
        dto.setMonthYear(message.getMonthYear());
        return dto;
    }
}