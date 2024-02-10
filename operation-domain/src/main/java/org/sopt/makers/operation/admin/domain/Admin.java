package org.sopt.makers.operation.admin.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AdminStatus status;

    @Column(name="refresh_token")
    private String refreshToken;

    @Builder
    public Admin(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.status = AdminStatus.NOT_CERTIFIED;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isNotAllowed() {
        return this.status.equals(AdminStatus.NOT_CERTIFIED);
    }
}
