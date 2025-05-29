package com.erp.repository;

import com.erp.entity.Employee;
import com.erp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEmployee(Employee employee);
}