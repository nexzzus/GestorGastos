package com.nexzus.gestiongastos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUser(
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 32, message = "El nombre debe tener entre {min} y {max} carácteres")
        String firstName,

        @NotBlank(message = "El apellido es requerido")
        @Size(min = 3, max = 32, message = "El apellido debe tener entre {min} y {max} carácteres")
        String lastName,

        @NotBlank(message = "El email es requerido")
        @Size(max = 64, message = "El email debe tener máximo {max} carácteres")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 3, max = 64, message = "La contraseña debe tener entre {min} y {max} carácteres")
        String password,

        @NotBlank(message = "La confirmación de contraseña es requerida")
        @Size(min = 3, max = 64, message = "La confirmación de contraseña debe tener entre {min} y {max} carácteres")
        String confirmPassword
) {
}
