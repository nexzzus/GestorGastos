package com.nexzus.gestiongastos.controller;

import com.nexzus.gestiongastos.dto.request.IncomeRequestDto;
import com.nexzus.gestiongastos.dto.response.IncomeResponseDto;
import com.nexzus.gestiongastos.security.jwt.JwtUtils;
import com.nexzus.gestiongastos.service.abstraction.IIncomeService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
public class IncomeController {
    private final IIncomeService incomeService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<IncomeResponseDto> create(@RequestBody @Valid IncomeRequestDto request,
                                                    @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtUtils.extractUserId(token);
        return new ResponseEntity<>(incomeService.create(request, userId), org.springframework.http.HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(incomeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<IncomeResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(incomeService.getAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        incomeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponseDto> update(@PathVariable UUID id,
                                                    @RequestBody @Valid IncomeRequestDto request,
                                                    @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearere ", "");
        UUID userId = jwtUtils.extractUserId(token);
        return ResponseEntity.ok(incomeService.updateById(id, request, userId));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<IncomeResponseDto>> getAllByUserId(
            Pageable pageable,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtUtils.extractUserId(token);
        return ResponseEntity.ok(incomeService.getAllByUserId(userId, pageable));
    }
}
