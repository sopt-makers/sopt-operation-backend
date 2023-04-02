package org.sopt.makers.operation.dto.admin;

import org.sopt.makers.operation.entity.Role;

public record SignUpRequestDTO(
        String email,
        String password,
        String name,
        Role role
) {
}
