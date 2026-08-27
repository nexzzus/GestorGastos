package com.nexzus.gestiongastos.dto.response;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
