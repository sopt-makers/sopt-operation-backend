package org.sopt.makers.operation.user.domain;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;

import org.sopt.makers.operation.common.domain.BaseEntity;
import org.sopt.makers.operation.user.dao.UserPersonalInfoUpdateDao;

@Entity @Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "birthday")
    private LocalDate birthday;


    @Builder
    public User(String email, String phone, Gender gender, String name, String profileImage, LocalDate birthday) {
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.name = name;
        this.profileImage = profileImage;
        this.birthday = birthday;
    }

    public void updateUserInfo(UserPersonalInfoUpdateDao infoUpdateDao) {
        this.name = infoUpdateDao.name();
        this.phone = infoUpdateDao.phone();
        this.profileImage = infoUpdateDao.profileImage();
    }
}
