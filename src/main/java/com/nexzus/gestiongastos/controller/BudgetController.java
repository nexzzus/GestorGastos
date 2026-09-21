package com.nexzus.gestiongastos.controller;

import com.nexzus.gestiongastos.dto.request.BudgetRequestDto;
import com.nexzus.gestiongastos.dto.response.BudgetResponseDto;
import com.nexzus.gestiongastos.service.abstraction.IBudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetController {
    private final IBudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseDto> create(@RequestBody @Valid BudgetRequestDto request) {
        return new ResponseEntity<>(budgetService.create(request), org.springframework.http.HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(budgetService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BudgetResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(budgetService.getAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDto> update(@PathVariable UUID id,
                                                    @RequestBody @Valid BudgetRequestDto request) {
        return ResponseEntity.ok(budgetService.update(id, request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BudgetResponseDto>> getAllByUserId(@PathVariable UUID userId, Pageable pageable) {
        return ResponseEntity.ok(budgetService.getAllByUserId(userId, pageable));
    }
}
