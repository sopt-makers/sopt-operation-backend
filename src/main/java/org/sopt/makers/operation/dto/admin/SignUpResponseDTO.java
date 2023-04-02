package org.sopt.makers.operation.dto.admin;

import org.sopt.makers.operation.entity.Role;

public record SignUpResponseDTO(
        Long id,
        String email,

        String name,
        Role role
) {
}
