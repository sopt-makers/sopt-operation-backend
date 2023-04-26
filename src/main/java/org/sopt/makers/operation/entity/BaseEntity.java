package org.sopt.makers.operation.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

	@LastModifiedDate
	private LocalDateTime lastModifiedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

	@PrePersist
	void prePersist() {
		createdDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		lastModifiedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}

	@PreUpdate
	void preUpdate() {
		lastModifiedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}
}
