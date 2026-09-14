package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.request.CategoryRequestDto;
import com.nexzus.gestiongastos.dto.response.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICategoryService {
    CategoryResponseDto create(CategoryRequestDto request);
    CategoryResponseDto getById(UUID id);
    void deleteById(UUID id);
    CategoryResponseDto updateById(UUID id, CategoryRequestDto request);
    Page<CategoryResponseDto> getAll(Pageable pageable);
}
