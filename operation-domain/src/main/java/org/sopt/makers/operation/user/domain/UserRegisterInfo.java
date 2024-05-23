package org.sopt.makers.operation.user.domain;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

@Entity @Getter
@Table(name = "user_register_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRegisterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "generation", nullable = false)
    private int generation;

    @Column(name = "phone")
    private String phone;

    @Column(name = "part")
    @Enumerated(value = EnumType.STRING)
    private Part part;

    @Builder
    public UserRegisterInfo(String name, String email, String phone, Part part, Integer generation) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.part = part;
        this.generation = generation;
    }
}
