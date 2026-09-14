package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.request.ExpenseRequestDto;
import com.nexzus.gestiongastos.dto.response.ExpenseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IExpenseService {
    ExpenseResponseDto create(ExpenseRequestDto request);
    ExpenseResponseDto getById(UUID id);
    void deleteById(UUID id);
    ExpenseResponseDto updateById(UUID id, ExpenseRequestDto request);
    Page<ExpenseResponseDto> getAll(Pageable pageable);
    Page<ExpenseResponseDto> getAllByUserId(UUID userId, Pageable pageable);
}
