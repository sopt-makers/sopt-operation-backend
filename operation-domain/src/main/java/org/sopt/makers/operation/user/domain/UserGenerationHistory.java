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
import org.sopt.makers.operation.user.dao.UserActivityInfoUpdateDao;

@Entity @Getter
@Table(name = "user_generation_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGenerationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "generation", nullable = false)
    private int generation;

    @Column(name = "part")
    @Enumerated(value = EnumType.STRING)
    private Part part;

    @Column(name = "team")
    @Enumerated(value = EnumType.STRING)
    private Team team;

    @Column(name = "position", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Position position;

    @Builder
    public UserGenerationHistory(Long userId, int generation, Part part, Team team, Position position) {
        this.userId = userId;
        this.generation = generation;
        this.part = part;
        this.team = team;
        this.position = position;
    }

    public void updateActivityInfo(UserActivityInfoUpdateDao activityInfoUpdateDao) {
        this.team = activityInfoUpdateDao.team();
    }
}
