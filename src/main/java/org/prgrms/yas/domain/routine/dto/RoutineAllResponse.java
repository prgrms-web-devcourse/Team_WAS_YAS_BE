package org.prgrms.yas.domain.routine.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineAllResponse {
	
	private Long id;
	private String name;
	private String color;
	private String emoji;
	private LocalDateTime startGoalTime;
	private Long durationGoalTime;
	
	@Builder
	public RoutineAllResponse(
			Long id, String name, String color, String emoji, LocalDateTime startGoalTime,
			Long durationGoalTime
	) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.emoji = emoji;
		this.startGoalTime = startGoalTime;
		this.durationGoalTime = durationGoalTime;
	}
}
