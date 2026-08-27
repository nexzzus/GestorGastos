package com.nexzus.gestiongastos.service.abstraction;

import com.nexzus.gestiongastos.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {
    User getMe(UserDetails userDetails);
}
