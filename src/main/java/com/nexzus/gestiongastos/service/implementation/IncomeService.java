package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.config.Mapper;
import com.nexzus.gestiongastos.dto.request.IncomeRequestDto;
import com.nexzus.gestiongastos.dto.response.IncomeResponseDto;
import com.nexzus.gestiongastos.exception.ResourceNotFoundException;
import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.Income;
import com.nexzus.gestiongastos.model.User;
import com.nexzus.gestiongastos.repository.CategoryRepository;
import com.nexzus.gestiongastos.repository.IncomeRepository;
import com.nexzus.gestiongastos.repository.UserRepository;
import com.nexzus.gestiongastos.service.abstraction.IIncomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeService implements IIncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Override
    public IncomeResponseDto create(IncomeRequestDto request, UUID userId) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", request.categoryId().toString()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId.toString()));

        Income newIncome = mapper.toEntity(request);
        newIncome.setCategory(category);
        newIncome.setUser(user);
        incomeRepository.save(newIncome);

        return mapper.toDto1(newIncome);
    }

    @Override
    public IncomeResponseDto getById(UUID id) {
        return incomeRepository.findById(id)
                .map(mapper::toDto1)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso", "id", id.toString()));
    }

    @Override
    public void deleteById(UUID id) {
        Income Income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso", "id", id.toString()));
        incomeRepository.delete(Income);
    }

    @Override
    public IncomeResponseDto updateById(UUID id, IncomeRequestDto request, UUID userId) {
        Income Income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso", "id", id.toString()));

        mapper.partialUpdate(request, Income);

        if (request.categoryId() != null && !request.categoryId().equals(Income.getCategory().getId())) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", request.categoryId().toString()));
            Income.setCategory(category);
        }

        if (userId != null && !userId.equals(Income.getUser().getId())) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId.toString()));
            Income.setUser(user);
        }

        Income updatedIncome = incomeRepository.save(Income);
        log.error("Ingreso {}", Income);
        log.error("Ingreso actualizado {}", updatedIncome);
        return mapper.toDto1(updatedIncome);
    }

    @Override
    public Page<IncomeResponseDto> getAll(Pageable pageable) {
        return incomeRepository.findAll(pageable)
                .map(mapper::toDto1);
    }

    @Override
    public Page<IncomeResponseDto> getAllByUserId(UUID userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario", "id", userId.toString());
        }

        return incomeRepository.findAllByUserId(userId, pageable)
                .map(mapper::toDto1);
    }
}
