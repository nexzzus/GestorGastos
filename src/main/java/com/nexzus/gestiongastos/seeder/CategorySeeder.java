package com.nexzus.gestiongastos.seeder;

import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.enums.CategoryEnum;
import com.nexzus.gestiongastos.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CategorySeeder implements CommandLineRunner {
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        log.info("Iniciando validación y carga de categorías (SEEDER)...");

        List<Category> categories = Arrays.asList(
                new Category().builder().name(CategoryEnum.ALIMENTACION).description("Comida y súper").build(),
                new Category().builder().name(CategoryEnum.INGRESO).description("Entradas de dinero").build(),
                new Category().builder().name(CategoryEnum.SERVICIOS).description("Facturas básicas").build(),
                new Category().builder().name(CategoryEnum.TRANSPORTE).description("Movilidad diaria").build(),
                new Category().builder().name(CategoryEnum.VIVIENDA).description("Gastos del hogar").build(),
                new Category().builder().name(CategoryEnum.SALUD).description("Salud y farmacia").build(),
                new Category().builder().name(CategoryEnum.EDUCACION).description("Cursos y estudios").build(),
                new Category().builder().name(CategoryEnum.ENTRETENIMIENTO).description("Ocio y diversión").build(),
                new Category().builder().name(CategoryEnum.ROPA).description("Moda y calzado").build(),
                new Category().builder().name(CategoryEnum.TECNOLOGIA).description("Apps y equipos").build()
        );

      List<Category> categoriesToSave = new ArrayList<>();

      for (Category category : categories){
          if (!categoryRepository.existsByName(category.getName())) {
              categoriesToSave.add(category);
          }
      }

        if (!categoriesToSave.isEmpty()) {
            categoryRepository.saveAll(categoriesToSave);
            log.info("Categorías cargadas correctamente: {}", categoriesToSave.size());
        } else {
            log.info("Todas las categorías ya existen en la base de datos. No se realizaron cambios.");
        }
        }
}
