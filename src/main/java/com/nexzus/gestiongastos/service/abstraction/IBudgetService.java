package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.request.BudgetRequestDto;
import com.nexzus.gestiongastos.dto.response.BudgetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IBudgetService {
    BudgetResponseDto create(BudgetRequestDto request, UUID userId);
    void delete(UUID id);
    BudgetResponseDto update(UUID id, BudgetRequestDto request, UUID userId);
    BudgetResponseDto getById(UUID id);
    Page<BudgetResponseDto> getAll(Pageable pageable);
    Page<BudgetResponseDto> getAllByUserId(UUID userId, Pageable pageable);
}
