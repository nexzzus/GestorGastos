package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.config.Mapper;
import com.nexzus.gestiongastos.dto.request.BudgetRequestDto;
import com.nexzus.gestiongastos.dto.response.BudgetResponseDto;
import com.nexzus.gestiongastos.exception.ResourceNotFoundException;
import com.nexzus.gestiongastos.model.Budget;
import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.User;
import com.nexzus.gestiongastos.repository.BudgetRepository;
import com.nexzus.gestiongastos.repository.CategoryRepository;
import com.nexzus.gestiongastos.repository.UserRepository;
import com.nexzus.gestiongastos.service.abstraction.IBudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BudgetService implements IBudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Override
    public BudgetResponseDto create(BudgetRequestDto request, UUID userId) {
        Budget newBudget = mapper.toEntity(request);

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", request.categoryId().toString()));
            newBudget.setCategory(category);

        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId.toString()));

        newBudget.setUser(user);
        budgetRepository.save(newBudget);

        return mapper.toDto1(newBudget);
    }

    @Override
    public void delete(UUID id) {
        if (!budgetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Presupuesto", "id", id.toString());
        }
        budgetRepository.deleteById(id);
    }

    @Override
    public BudgetResponseDto update(UUID id, BudgetRequestDto request, UUID userId) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "id", id.toString()));

        mapper.partialUpdate(request, budget);

        if (request.categoryId() != null && !request.categoryId().equals(budget.getCategory().getId())) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", request.categoryId().toString()));
            budget.setCategory(category);
        }

        if (userId != null && !userId.equals(budget.getUser().getId())) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId.toString()));
            budget.setUser(user);
        }

        Budget updatedBudget = budgetRepository.save(budget);
        log.error("Presupuesto {}", budget);
        log.error("Presupuesto actualizado {}", updatedBudget);
        return mapper.toDto1(updatedBudget);
    }

    @Override
    public BudgetResponseDto getById(UUID id) {
        return budgetRepository.findById(id)
                .map(mapper::toDto1)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto", "id", id.toString()));
    }

    @Override
    public Page<BudgetResponseDto> getAll(Pageable pageable) {
        return budgetRepository.findAll(pageable)
                .map(mapper::toDto1);
    }

    @Override
    public Page<BudgetResponseDto> getAllByUserId(UUID userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario", "id", userId.toString());
        }

        return budgetRepository.findAllByUserId(userId, pageable)
                .map(mapper::toDto1);
    }
}
