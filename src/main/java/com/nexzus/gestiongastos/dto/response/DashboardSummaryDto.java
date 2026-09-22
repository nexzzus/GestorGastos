package com.nexzus.gestiongastos.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardSummaryDto(
        BigDecimal totalBalance,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        List<CategoryExpenseSummaryDto> expensesByCategory,
        List<TransactionSummaryDto> recentActivity
) {
}
