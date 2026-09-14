package com.nexzus.gestiongastos.config;

import com.nexzus.gestiongastos.dto.request.CreateExpenseDto;
import com.nexzus.gestiongastos.dto.request.CreateUser;
import com.nexzus.gestiongastos.dto.response.ExpenseResponseDto;
import com.nexzus.gestiongastos.dto.response.UserResponse;
import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.dto.request.CreateCategoryDto;
import com.nexzus.gestiongastos.model.CategoryResponseDto;
import com.nexzus.gestiongastos.model.Expense;
import com.nexzus.gestiongastos.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@org.mapstruct.Mapper(componentModel = "spring", uses = {Mapper.class, Mapper.class})
public interface Mapper {
    // CREATE_USER TO USER
    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUser request);

    // USER TO USER_RESPONSE
    UserResponse toUserResponse(User user);

    Category toEntity(CreateCategoryDto createCategoryDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(CreateCategoryDto createCategoryDto, @MappingTarget Category category);

    Category toEntity(CategoryResponseDto categoryResponseDto);

    CategoryResponseDto toDto1(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(CategoryResponseDto categoryResponseDto, @MappingTarget Category category);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "categoryId", target = "category.id")
    Expense toEntity(CreateExpenseDto createExpenseDto);

    @InheritInverseConfiguration(name = "toEntity")
    CreateExpenseDto toDto(Expense expense);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Expense partialUpdate(CreateExpenseDto createExpenseDto, @MappingTarget Expense expense);

    User toEntity(UserResponse userResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserResponse userResponse, @MappingTarget User user);

    Expense toEntity(ExpenseResponseDto expenseResponseDto);

    ExpenseResponseDto toDto1(Expense expense);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Expense partialUpdate(ExpenseResponseDto expenseResponseDto, @MappingTarget Expense expense);
}
