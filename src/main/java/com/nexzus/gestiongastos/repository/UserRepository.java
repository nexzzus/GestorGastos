package com.nexzus.gestiongastos.repository;

import com.nexzus.gestiongastos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsUserByEmail(String email);

    User findUserByEmail(String email);

    Optional<User> findByEmail(String email);
}
