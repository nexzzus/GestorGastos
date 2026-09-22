package com.nexzus.gestiongastos.dto.request;

import com.nexzus.gestiongastos.model.enums.PaymentMethodEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncomeRequestDto(@NotNull(message = "El título es requerido")
                               @Size(message = "El título debe tener entre {min} y {max} carácteres", min = 3, max = 64)
                               String title,

                               @NotNull(message = "El monto es requerido")
                               @Positive(message = "El monto debe ser mayor a cero")
                               BigDecimal amount,

                               @NotNull(message = "El ID de la categoría es requerido")
                               UUID categoryId,

                               LocalDateTime createdAt,

                               String description,
                               PaymentMethodEnum paymentMethod
) implements Serializable {
}