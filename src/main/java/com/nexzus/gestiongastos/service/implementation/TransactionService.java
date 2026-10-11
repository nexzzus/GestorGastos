package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.dto.response.CategoryResponseDto;
import com.nexzus.gestiongastos.dto.response.TransactionResponseDto;
import com.nexzus.gestiongastos.model.enums.CategoryEnum;
import com.nexzus.gestiongastos.repository.TransactionRepository;
import com.nexzus.gestiongastos.repository.projection.TransactionProjection;
import com.nexzus.gestiongastos.service.abstraction.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Slice<TransactionResponseDto> getTransactions(UUID userId, int page, int size) {
        // Pedimos uno adicional para saber si existe otra página
        PageRequest pageable = PageRequest.of(page, size + 1);

        List<TransactionProjection> projections =
                transactionRepository.findTransactionsByUserId(
                        userId,
                        pageable
                );

        boolean hasNext = projections.size() > size;

        if (hasNext) {
            projections = projections.subList(0, size);
        }

        List<TransactionResponseDto> transactions =
                projections.stream()
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
