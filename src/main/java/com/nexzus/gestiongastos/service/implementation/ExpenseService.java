package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.config.Mapper;
import com.nexzus.gestiongastos.dto.request.ExpenseRequestDto;
import com.nexzus.gestiongastos.dto.response.ExpenseResponseDto;
import com.nexzus.gestiongastos.exception.ResourceNotFoundException;
import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.Expense;
import com.nexzus.gestiongastos.model.User;
import com.nexzus.gestiongastos.repository.CategoryRepository;
import com.nexzus.gestiongastos.repository.ExpenseRepository;
import com.nexzus.gestiongastos.repository.UserRepository;
import com.nexzus.gestiongastos.service.abstraction.IExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService implements IExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Override
    public ExpenseResponseDto create(ExpenseRequestDto request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", request.categoryId().toString()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.userId().toString()));

        Expense newExpense = mapper.toEntity(request);
        newExpense.setCategory(category);
        newExpense.setUser(user);
        expenseRepository.save(mapper.toEntity(request));

        return mapper.toDto1(newExpense);
    }

    @Override
    public ExpenseResponseDto getById(UUID id) {
        return expenseRepository.findById(id)
                .map(mapper::toDto1)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", id.toString()));
    }

    @Override
    public void deleteById(UUID id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", id.toString()));
        expenseRepository.delete(expense);
    }

    @Override
    public ExpenseResponseDto updateById(UUID id, ExpenseRequestDto request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gasto", "id", id.toString()));

        mapper.partialUpdate(request, expense);

        if (request.categoryId() != null && !request.categoryId().equals(expense.getCategory().getId())) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", request.categoryId().toString()));
            expense.setCategory(category);
        }

        if (request.userId() != null && !request.userId().equals(expense.getUser().getId())) {
            User user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.userId().toString()));
            expense.setUser(user);
        }

        Expense updatedExpense = expenseRepository.save(expense);
        log.error("Gasto {}", expense);
        log.error("Gasto actualizado {}", updatedExpense);
        return mapper.toDto1(updatedExpense);
    }

    @Override
    public Page<ExpenseResponseDto> getAll(Pageable pageable) {
        return expenseRepository.findAll(pageable)
                .map(mapper::toDto1);
    }

    @Override
    public Page<ExpenseResponseDto> getAllByUserId(UUID userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario", "id", userId.toString());
        }

        return expenseRepository.findAllByUserId(userId, pageable)
                .map(mapper::toDto1);
    }
}
