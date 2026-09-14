package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.request.CreateCategoryDto;
import com.nexzus.gestiongastos.model.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICategoryService {
    CategoryResponseDto create(CreateCategoryDto request);
    CategoryResponseDto getById(UUID id);
    void deleteById(UUID id);
    CategoryResponseDto updateById(UUID id, CreateCategoryDto request);
    Page<CategoryResponseDto> getAll(Pageable pageable);
}
