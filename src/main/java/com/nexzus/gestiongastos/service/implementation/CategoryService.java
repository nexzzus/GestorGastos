package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.config.Mapper;
import com.nexzus.gestiongastos.dto.request.CategoryRequestDto;
import com.nexzus.gestiongastos.exception.DuplicateResourceException;
import com.nexzus.gestiongastos.exception.ResourceNotFoundException;
import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.dto.response.CategoryResponseDto;
import com.nexzus.gestiongastos.repository.CategoryRepository;
import com.nexzus.gestiongastos.service.abstraction.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Override
    public CategoryResponseDto create(CategoryRequestDto request) {
        if (categoryRepository.existsByName(request.name())){
            throw new DuplicateResourceException("Categoría", "nombre", request.name());
        }
        Category newCategory = categoryRepository.save(mapper.toEntity(request));
        return mapper.toDto1(newCategory);
    }

    @Override
    public CategoryResponseDto getById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", id.toString()));

        return mapper.toDto1(category);
    }

    @Override
    public void deleteById(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría", "id", id.toString());
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponseDto updateById(UUID id, CategoryRequestDto request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", id.toString()));

        if (categoryRepository.existsByName(request.name()) && !category.getName().equals(request.name())) {
            throw new DuplicateResourceException("Categoría", "nombre", request.name());
        }

        category.setName(request.name());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.toDto1(updatedCategory);
    }

    @Override
    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(mapper::toDto1);
    }
}
