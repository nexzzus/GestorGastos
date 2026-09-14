package com.nexzus.gestiongastos.controller;

import com.nexzus.gestiongastos.dto.request.CreateCategoryDto;
import com.nexzus.gestiongastos.model.CategoryResponseDto;
import com.nexzus.gestiongastos.service.abstraction.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody @Valid CreateCategoryDto request){
        return new ResponseEntity<>(categoryService.create(request), HttpStatus.CREATED);
    }
}
