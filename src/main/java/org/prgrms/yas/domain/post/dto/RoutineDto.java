package org.prgrms.yas.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Routine;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineDto {
	
	private Long routineId;
	private String name;
	private String emoji;
	private Long durationGoalTime;
	private String color;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private ZonedDateTime startGoalTime;
	private List<String> weeks;
	private List<String> category;
	private List<MissionDto> missions = new ArrayList<>();
	
	@Builder
	public RoutineDto(final Routine routine) {
		this.routineId = routine.getId();
		this.name = routine.getName();
		this.emoji = routine.getEmoji();
		this.durationGoalTime = routine.getDurationGoalTime();
		this.color = routine.getColor();
		this.startGoalTime = routine.getStartGoalTime();
		this.weeks = routine.getStringWeeks(routine.getWeeks());
		this.category = routine.getStringCategory(routine.getRoutineCategory());
		this.missions = routine.getMissions()
		                       .stream()
		                       .map(MissionDto::new)
		                       .collect(Collectors.toList());
	}
}
