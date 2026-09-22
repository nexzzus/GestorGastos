package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.request.IncomeRequestDto;
import com.nexzus.gestiongastos.dto.response.IncomeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IIncomeService {
    IncomeResponseDto create(IncomeRequestDto request, UUID userId);
    IncomeResponseDto getById(UUID id);
    void deleteById(UUID id);
    IncomeResponseDto updateById(UUID id, IncomeRequestDto request, UUID userId);
    Page<IncomeResponseDto> getAll(Pageable pageable);
    Page<IncomeResponseDto> getAllByUserId(UUID userId, Pageable pageable);
}
