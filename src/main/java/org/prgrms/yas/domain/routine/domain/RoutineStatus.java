package org.prgrms.yas.domain.routine.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
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
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "routine_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private ZonedDateTime startTime;
	
	private ZonedDateTime endTime;
	
	private ZonedDateTime dateTime;
	
	@ColumnDefault("-1")
	private Long userDurationTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_id")
	private Routine routine;
	
	@Builder
	public RoutineStatus(
			ZonedDateTime startTime, ZonedDateTime endTime, Long userDurationTime, Routine routine,
			ZonedDateTime dateTime
	) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.userDurationTime = userDurationTime;
		this.routine = routine;
		this.dateTime = dateTime;
	}
	
	public void setRoutine(Routine routine) {
		if (Objects.nonNull(this.routine)) {
			this.routine.getRoutineStatuses()
			            .remove(this);
		}
		this.routine = routine;
	}
	
	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}
	
	public void setEndTimeIsNull() {
		this.endTime = null;
	}
	
	public void setUserDurationTime(Long userDurationTime) {
		this.userDurationTime = userDurationTime;
	}
}
