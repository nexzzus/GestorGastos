package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {
    UserResponse getMe(UserDetails userDetails);
}
