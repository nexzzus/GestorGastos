package com.nexzus.gestiongastos.repository;

import com.nexzus.gestiongastos.dto.response.ExpenseResponseDto;
import com.nexzus.gestiongastos.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    Page<Expense> findAllByUserId(UUID userId, Pageable pageable);
}