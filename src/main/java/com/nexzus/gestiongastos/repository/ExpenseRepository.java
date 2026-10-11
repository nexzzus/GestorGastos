package com.nexzus.gestiongastos.repository;

import com.nexzus.gestiongastos.dto.response.CategoryExpenseSummaryDto;
import com.nexzus.gestiongastos.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    Page<Expense> findAllByUserId(UUID userId, Pageable pageable);

    // Suma total de gastos
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId " +
            "AND e.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalByUserId(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // Gastos agrupados por categoría
    @Query("SELECT" +
            " new com.nexzus.gestiongastos.dto.response.CategoryExpenseSummaryDto(CAST(e.category.name AS string), SUM(e.amount)) " +
            "FROM Expense e WHERE e.user.id = :userId AND e.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY e.category.name")
    List<CategoryExpenseSummaryDto> sumByCategory(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Obtener los N más recientes
    List<Expense> findTop5ByUserIdOrderByCreatedAtDesc(UUID userId);
}