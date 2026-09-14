package com.nexzus.gestiongastos.repository;

import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(CategoryEnum name);

    @Override
    Optional<Category> findById(UUID id);

}