package org.sopt.makers.operation.entity;

import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class SubSession {

	@Id @GeneratedValue(strategy = IDENTITY)
	@Column(name = "sub_session_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id")
	private Session session;

	private int order;

	private LocalDateTime startAt;

	@OneToMany(mappedBy = "subSession")
	private List<SubAttendance> subAttendances = new ArrayList<>();

	public SubSession(Session session, int order, LocalDateTime startAt) {
		this.session = session;
		this.order = order;
		this.startAt = startAt;
	}
}
