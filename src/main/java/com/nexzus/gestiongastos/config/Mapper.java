package com.nexzus.gestiongastos.config;

import com.nexzus.gestiongastos.dto.request.CreateUser;
import com.nexzus.gestiongastos.dto.response.UserResponse;
import com.nexzus.gestiongastos.model.User;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    // CREATE_USER TO USER
    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUser request);

    // USER TO USER_RESPONSE
    UserResponse toUserResponse(User user);
}
