package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.config.Mapper;
import com.nexzus.gestiongastos.dto.request.CreateCategoryDto;
import com.nexzus.gestiongastos.exception.DuplicateResourceException;
import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.CategoryResponseDto;
import com.nexzus.gestiongastos.repository.CategoryRepository;
import com.nexzus.gestiongastos.service.abstraction.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Override
    public CategoryResponseDto create(CreateCategoryDto request) {
        if (categoryRepository.existsByName(request.name())){
            throw new DuplicateResourceException("Categoría", "nombre", request.name());
        }
        Category newCategory = categoryRepository.save(mapper.toEntity(request));
        return mapper.toDto1(newCategory);
    }
}
