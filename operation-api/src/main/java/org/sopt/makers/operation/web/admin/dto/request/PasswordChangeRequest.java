package org.sopt.makers.operation.web.admin.dto.request;

public record PasswordChangeRequest(
        String oldPassword,
        String newPassword
) { }
