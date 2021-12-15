package org.prgrms.yas.domain.post.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineDto {
	
	private Long routineId;
	private String name;
	private String emoji;
	private Long durationGoalTime;
	private List<String> category;
	private List<MissionDto> missions = new ArrayList<>();
	
	@Builder
	public RoutineDto(final Routine routine) {
		this.routineId = routine.getId();
		this.name = routine.getName();
		this.emoji = routine.getEmoji();
		this.durationGoalTime = routine.getDurationGoalTime();
		this.category = routine.getRoutineCategory()
		                       .stream()
		                       .map(CategoryDto::categoryToString)
		                       .collect(Collectors.toList());
		this.missions = routine.getMissions()
		                       .stream()
		                       .map(MissionDto::new)
		                       .collect(Collectors.toList());
	}
}
