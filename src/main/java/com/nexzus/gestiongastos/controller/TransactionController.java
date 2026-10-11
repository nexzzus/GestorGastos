package com.nexzus.gestiongastos.controller;

import com.nexzus.gestiongastos.dto.response.TransactionResponseDto;
import com.nexzus.gestiongastos.security.jwt.JwtUtils;
import com.nexzus.gestiongastos.service.abstraction.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService transactionService;
    private final JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<Slice<TransactionResponseDto>> getTransactions(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtUtils.extractUserId(token);

        return new ResponseEntity<>(transactionService.getTransactions(userId, page, size), HttpStatus.OK);
    }
}
