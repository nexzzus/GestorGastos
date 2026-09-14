package com.nexzus.gestiongastos.controller;

import com.nexzus.gestiongastos.dto.request.ExpenseRequestDto;
import com.nexzus.gestiongastos.dto.response.ExpenseResponseDto;
import com.nexzus.gestiongastos.service.abstraction.IExpenseService;
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
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {
    private final IExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> create(@RequestBody @Valid ExpenseRequestDto request) {
        return new ResponseEntity<>(expenseService.create(request), org.springframework.http.HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(expenseService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(expenseService.getAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        expenseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> update(@PathVariable UUID id,
                                                     @RequestBody @Valid ExpenseRequestDto request) {
        return ResponseEntity.ok(expenseService.updateById(id, request));
    }
}
