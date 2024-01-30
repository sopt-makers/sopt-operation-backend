package org.sopt.makers.operation.dto.admin.response;

import org.sopt.makers.operation.entity.admin.Role;

public record SignUpResponseDTO(
        Long id,
        String email,
        String name,
        Role role
) {
}
