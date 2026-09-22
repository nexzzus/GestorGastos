package com.nexzus.gestiongastos.repository;

import com.nexzus.gestiongastos.dto.response.CategoryExpenseSummaryDto;
import com.nexzus.gestiongastos.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IncomeRepository extends JpaRepository<Income, UUID> {
    Page<Income> findAllByUserId(UUID userId, Pageable pageable);

    // Suma total de ingresos
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Income e WHERE e.user.id = :userId")
    BigDecimal sumTotalByUserId(@Param("userId") UUID userId);

    // Gastos agrupados por categoría
    @Query("SELECT new com.nexzus.gestiongastos.dto.response.CategoryExpenseSummaryDto(CAST(e.category.name AS string), SUM(e.amount)) " +
            "FROM Income e WHERE e.user.id = :userId GROUP BY e.category.name")
    List<CategoryExpenseSummaryDto> sumByCategory(@Param("userId") UUID userId);

    // Obtener los N más recientes
    List<Income> findTop5ByUserIdOrderByCreatedAtDesc(UUID userId);
}