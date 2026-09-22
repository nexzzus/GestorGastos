package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.dto.response.CategoryExpenseSummaryDto;
import com.nexzus.gestiongastos.dto.response.CategoryResponseDto;
import com.nexzus.gestiongastos.dto.response.DashboardSummaryDto;
import com.nexzus.gestiongastos.dto.response.TransactionSummaryDto;
import com.nexzus.gestiongastos.repository.ExpenseRepository;
import com.nexzus.gestiongastos.repository.IncomeRepository;
import com.nexzus.gestiongastos.service.abstraction.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public DashboardSummaryDto getDashboardSummary(UUID userId) {

        // 1. Obtener totales
        BigDecimal totalExpense = expenseRepository.sumTotalByUserId(userId);
        BigDecimal totalIncome = incomeRepository.sumTotalByUserId(userId);
        BigDecimal totalBalance = totalIncome.subtract(totalExpense);

        // 2. Obtener gastos agrupados por categoría
        List<CategoryExpenseSummaryDto> expensesByCategory = expenseRepository.sumByCategory(userId);

        // 3. Unificar Actividad Reciente
        List<TransactionSummaryDto> transactions = new ArrayList<>();

        // Mapear gastos (Expense)
        expenseRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId).forEach(e ->
                transactions.add(new TransactionSummaryDto(
                        e.getId(), e.getTitle(), e.getAmount(), e.getCreatedAt(), "EXPENSE",
                        new CategoryResponseDto(e.getCategory().getId(), e.getCategory().getName())
                ))
        );

        // Mapear ingresos (Income)
        incomeRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId).forEach(i ->
                transactions.add(new TransactionSummaryDto(
                        i.getId(), i.getTitle(), i.getAmount(), i.getCreatedAt(), "INCOME",
                        new CategoryResponseDto(i.getCategory().getId(), i.getCategory().getName())
                ))
        );

        // 4. Ordenar la lista unificada por fecha descendente y tomar solo los 5 más recientes
        List<TransactionSummaryDto> recentActivity = transactions.stream()
                .sorted(Comparator.comparing(TransactionSummaryDto::createdAt).reversed())
                .limit(5)
                .toList();

        return new DashboardSummaryDto(
                totalBalance, totalIncome, totalExpense, expensesByCategory, recentActivity
        );
    }
}