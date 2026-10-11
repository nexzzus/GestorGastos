package com.nexzus.gestiongastos.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link com.nexzus.gestiongastos.model.Budget}
 */
public record BudgetRequestDto(
        @NotNull(message = "El monto es requerido")
        @Positive(message = "El monto debe ser mayor a cero")
        BigDecimal amount,

//        @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a hoy")
        @NotNull(message = "La fecha de inicio es requerida")
        LocalDate startDate,

        @Future(message = "La fecha de fin debe ser futura")
        @NotNull(message = "La fecha de fin es requerida")
        LocalDate endDate,


        UUID categoryId
) implements Serializable {
}