package com.nexzus.gestiongastos.config;

import com.nexzus.gestiongastos.dto.request.CreateUser;
import com.nexzus.gestiongastos.dto.response.UserResponse;
import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.dto.request.CreateCategoryDto;
import com.nexzus.gestiongastos.model.CategoryResponseDto;
import com.nexzus.gestiongastos.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@org.mapstruct.Mapper(componentModel = "spring")
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
}
