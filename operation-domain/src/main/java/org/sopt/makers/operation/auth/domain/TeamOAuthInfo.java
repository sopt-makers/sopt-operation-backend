package org.sopt.makers.operation.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class TeamOAuthInfo {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "client_id", nullable = false)
    private String clientId;
    @Column(name = "redirect_uri", nullable = false)
    private String redirectUri;
    @Column(name = "team", nullable = false)
    @Enumerated(EnumType.STRING)
    private Team team;
}
