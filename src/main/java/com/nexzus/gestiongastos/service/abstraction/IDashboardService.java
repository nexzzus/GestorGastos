package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.response.DashboardSummaryDto;

import java.util.UUID;

public interface IDashboardService {
    DashboardSummaryDto getDashboardSummary(UUID userId);

}
