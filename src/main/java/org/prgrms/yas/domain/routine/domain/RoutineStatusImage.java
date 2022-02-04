package org.prgrms.yas.domain.routine.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routine_status_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineStatusImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_status_id")
	private RoutineStatus routineStatus;
	
	@Column(columnDefinition = "TEXT")
	private String reviewImage;
	
	@Builder
	public RoutineStatusImage(
			RoutineStatus routineStatus, String reviewImage
	) {
		
		this.routineStatus = routineStatus;
		this.reviewImage = reviewImage;
	}
	
	public void setRoutineStatus(RoutineStatus routine_status) {
		this.routineStatus = routine_status;
	}
	
}
