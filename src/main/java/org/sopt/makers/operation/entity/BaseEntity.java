package org.sopt.makers.operation.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

	@CreatedDate
	@JsonFormat(timezone = "Asia/Seoul")
	@Column(updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@JsonFormat(timezone = "Asia/Seoul")
	private LocalDateTime lastModifiedDate;
}
