package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.request.CreateCategoryDto;
import com.nexzus.gestiongastos.model.CategoryResponseDto;

public interface ICategoryService {
    CategoryResponseDto create(CreateCategoryDto request);
}
