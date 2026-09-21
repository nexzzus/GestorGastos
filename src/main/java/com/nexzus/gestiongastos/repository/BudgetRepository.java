package com.nexzus.gestiongastos.repository;

import com.nexzus.gestiongastos.model.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
    Page<Budget> findAllByUserId(UUID userId, Pageable pageable);
}