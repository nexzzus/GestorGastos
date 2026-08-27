package com.nexzus.gestiongastos.service.implementation;

import com.nexzus.gestiongastos.exception.ResourceNotFoundException;
import com.nexzus.gestiongastos.model.User;
import com.nexzus.gestiongastos.repository.UserRepository;
import com.nexzus.gestiongastos.service.abstraction.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public User getMe(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("User", "email", userDetails.getUsername()));

        return user;
    }
}
