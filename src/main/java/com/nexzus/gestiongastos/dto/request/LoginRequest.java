package com.nexzus.gestiongastos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El email es requerido")
        @Size(max = 64, message = "El email debe tener máximo {max} carácteres")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 3, max = 64, message = "La contraseña debe tener entre {min} y {max} carácteres")
        String password
) {
}
