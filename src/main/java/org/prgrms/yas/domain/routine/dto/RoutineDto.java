package org.prgrms.yas.domain.routine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineDto {
	
	private String name;
	private String emoji;
	private String color;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private ZonedDateTime startGoalTime;
	private Long durationGoalTime;
	private boolean isPosted;
	private List<String> routineCategory;
	private List<String> weeks;
	
	@Builder
	public RoutineDto(
			String name, String emoji, String color, ZonedDateTime startGoalTime, Long durationGoalTime,
			boolean isPosted, List<String> routineCategory, List<String> weeks
	) {
		this.name = name;
		this.emoji = emoji;
		this.color = color;
		this.startGoalTime = startGoalTime;
		this.durationGoalTime = durationGoalTime;
		this.isPosted = isPosted;
		this.routineCategory = routineCategory;
		this.weeks = weeks;
	}
}
