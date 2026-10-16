package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.dto.response.CategoryResponseDto;
import com.nexzus.gestiongastos.dto.response.TransactionResponseDto;
import com.nexzus.gestiongastos.model.enums.CategoryEnum;
import com.nexzus.gestiongastos.repository.TransactionRepository;
import com.nexzus.gestiongastos.repository.projection.TransactionProjection;
import com.nexzus.gestiongastos.service.abstraction.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Slice<TransactionResponseDto> getTransactions(UUID userId, int page, int size) {
        // Retrieve exactly 'size' items for the current page
        PageRequest pageable = PageRequest.of(page, size);
        List<TransactionProjection> projections = transactionRepository.findTransactionsByUserId(userId, pageable);

        // Determine if there is a next page by checking if there is at least one item in the following page
        boolean hasNext = false;
        if (projections.size() == size) {
            // Check if the next page has any records
            List<TransactionProjection> nextPage = transactionRepository.findTransactionsByUserId(
                    userId,
                    PageRequest.of(page + 1, 1)
            );
            hasNext = !nextPage.isEmpty();
        }

        List<TransactionResponseDto> transactions = projections.stream()
                .map(this::toDto)
                .toList();

        return new SliceImpl<>(
                transactions,
                PageRequest.of(page, size),
                hasNext
        );
    }

    private TransactionResponseDto toDto(TransactionProjection projection) {
        CategoryResponseDto category = new CategoryResponseDto(
                projection.getId(),
                CategoryEnum.valueOf(projection.getCategoryName()),
                projection.getCategoryDescription()
        );
        return new TransactionResponseDto(
                projection.getId(),
                projection.getTitle(),
                projection.getAmount(),
                projection.getCreatedAt(),
                projection.getDescription(),
                projection.getPaymentMethod(),
                category,
                projection.getType()
        );
    }
}
