package org.sopt.makers.operation.user.dao;

public record UserPersonalInfoUpdateDao(
    String name,
    String phone,
    String profileImage
) {
}
