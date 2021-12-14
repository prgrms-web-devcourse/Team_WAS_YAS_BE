package org.prgrms.yas.domain.routine.domain;

import java.time.LocalDateTime;
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
	
	private LocalDateTime startTime;
	
	private LocalDateTime endTime;
	
	@ColumnDefault("-1")
	private Long userDurationTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_id")
	private Routine routine;
	
	@Builder
	public RoutineStatus(
			Long id, LocalDateTime startTime, LocalDateTime endTime, Long userDurationTime,
			Routine routine
	) {
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.userDurationTime = userDurationTime;
		this.routine = routine;
	}
	
	public void setRoutine(Routine routine) {
		if (Objects.nonNull(this.routine)) {
			this.routine.getRoutineStatuses()
			            .remove(this);
		}
		this.routine = routine;
	}
	
}
