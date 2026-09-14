package com.nexzus.gestiongastos.repository;

import com.nexzus.gestiongastos.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
}