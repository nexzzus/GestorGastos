package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.dto.response.CategoryBudgetSummaryDto;
import com.nexzus.gestiongastos.dto.response.CategoryExpenseSummaryDto;
import com.nexzus.gestiongastos.dto.response.CategoryResponseDto;
import com.nexzus.gestiongastos.dto.response.DashboardSummaryDto;
import com.nexzus.gestiongastos.dto.response.TransactionSummaryDto;
import com.nexzus.gestiongastos.model.Budget;
import com.nexzus.gestiongastos.repository.BudgetRepository;
import com.nexzus.gestiongastos.repository.ExpenseRepository;
import com.nexzus.gestiongastos.repository.IncomeRepository;
import com.nexzus.gestiongastos.service.abstraction.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final BudgetRepository budgetRepository;

    public DashboardSummaryDto getDashboardSummary(UUID userId) {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

        BigDecimal totalExpense = expenseRepository.sumTotalByUserId(userId, startDate, endDate);
        BigDecimal totalIncome = incomeRepository.sumTotalByUserId(userId, startDate, endDate);
        BigDecimal totalBalance = totalIncome.subtract(totalExpense);

        List<CategoryExpenseSummaryDto> expensesByCategory = expenseRepository.sumByCategory(userId, startDate, endDate);

        List<Budget> budgets = budgetRepository.findAllByUserId(userId, Pageable.unpaged()).getContent();
        List<CategoryBudgetSummaryDto> budgetsByCategory = budgets.stream()
                .filter(budget -> budget.getCategory() != null)
                .map(budget -> {
                    BigDecimal budgetAmount = budget.getAmount() == null ? BigDecimal.ZERO : budget.getAmount();
                    LocalDate start = budget.getStartDate();
                    LocalDate end = budget.getEndDate();

                    LocalDateTime budgetStart = start != null ? start.atStartOfDay() : startDate;
                    LocalDateTime budgetEnd = end != null ? end.atTime(LocalTime.MAX) : endDate;

                    BigDecimal spentAmount = budgetRepository.sumExpensesPorBudgets(
                            userId,
                            budget.getCategory().getId(),
                            budgetStart,
                            budgetEnd
                    );

                    BigDecimal remainingAmount = budgetAmount.subtract(spentAmount);
                    BigDecimal usedPercentage = BigDecimal.ZERO;

                    if (budgetAmount.compareTo(BigDecimal.ZERO) > 0) {
                        usedPercentage = spentAmount.divide(budgetAmount, 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100));
                    }

                    return new CategoryBudgetSummaryDto(
                            budget.getCategory().getId(),
                            budget.getCategory().getName().name(),
                            budgetAmount,
                            spentAmount,
                            remainingAmount,
                            usedPercentage,
                           budgetEnd
                    );
                })
                .toList();

        List<TransactionSummaryDto> transactions = new ArrayList<>();

        expenseRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId).forEach(e ->
                transactions.add(new TransactionSummaryDto(
                        e.getId(), e.getTitle(), e.getAmount(), e.getCreatedAt(), "EXPENSE",
                        new CategoryResponseDto(e.getCategory().getId(), e.getCategory().getName(), e.getCategory().getDescription())
                ))
        );

        incomeRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId).forEach(i ->
                transactions.add(new TransactionSummaryDto(
                        i.getId(), i.getTitle(), i.getAmount(), i.getCreatedAt(), "INCOME",
                        new CategoryResponseDto(i.getCategory().getId(), i.getCategory().getName(), i.getCategory().getDescription())
                ))
        );

        List<TransactionSummaryDto> recentActivity = transactions.stream()
                .sorted(Comparator.comparing(TransactionSummaryDto::createdAt).reversed())
                .limit(5)
                .toList();

        return new DashboardSummaryDto(
                totalBalance, totalIncome, totalExpense, expensesByCategory, budgetsByCategory, recentActivity);
    }
}