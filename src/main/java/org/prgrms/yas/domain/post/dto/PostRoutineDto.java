package org.prgrms.yas.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Routine;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRoutineDto {
	
	private Long routineId;
	private String name;
	private String emoji;
	private Long durationGoalTime;
	private String color;
	private List<String> category;
	private List<String> week;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private ZonedDateTime startGoalTime;
	
	@Builder
	public PostRoutineDto(final Routine routine) {
		this.routineId = routine.getId();
		this.name = routine.getName();
		this.emoji = routine.getEmoji();
		this.durationGoalTime = routine.getDurationGoalTime();
		this.category = routine.getRoutineCategory()
		                       .stream()
		                       .map(CategoryDto::categoryToString)
		                       .collect(Collectors.toList());
		this.color = routine.getColor();
		this.week = routine.getWeeks()
		                   .stream()
		                   .map(WeekDto::weekToString)
		                   .collect(Collectors.toList());
		this.startGoalTime = routine.getStartGoalTime();
	}
	
}
