package com.nexzus.gestiongastos.config;

import com.nexzus.gestiongastos.dto.request.CategoryRequestDto;
import com.nexzus.gestiongastos.dto.request.CreateUser;
import com.nexzus.gestiongastos.dto.request.ExpenseRequestDto;
import com.nexzus.gestiongastos.dto.response.CategoryResponseDto;
import com.nexzus.gestiongastos.dto.response.ExpenseResponseDto;
import com.nexzus.gestiongastos.dto.response.UserResponse;
import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.Expense;
import com.nexzus.gestiongastos.model.User;
import org.mapstruct.BeanMapping;
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

    Category toEntity(CategoryRequestDto createCategoryDto);

    CategoryResponseDto toDto1(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "categoryId", target = "category.id")
    Expense toEntity(ExpenseRequestDto createExpenseDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    Expense partialUpdate(ExpenseRequestDto createExpenseDto, @MappingTarget Expense expense);

    ExpenseResponseDto toDto1(Expense expense);
}
