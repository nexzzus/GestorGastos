package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.response.TransactionResponseDto;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface ITransactionService {
    Slice<TransactionResponseDto> getTransactions(UUID userId, int page, int size);
}
