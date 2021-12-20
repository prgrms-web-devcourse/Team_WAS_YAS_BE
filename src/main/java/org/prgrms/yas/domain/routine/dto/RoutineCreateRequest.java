package org.prgrms.yas.domain.routine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
import org.prgrms.yas.domain.routine.domain.Week;
import org.prgrms.yas.domain.user.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineCreateRequest {
	
	private String name;
	private List<String> routineCategory;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",timezone = "Asia/Seoul")
	private LocalDateTime startGoalTime;
	private Long durationGoalTime;
	private List<String> weeks;
	private String emoji;
	private String color;
	
	@Builder
	public RoutineCreateRequest(
			String name, LocalDateTime startGoalTime, Long durationGoalTime, List<String> weeks,
			List<String> routineCategory, String color, String emoji
	) {
		this.name = name;
		this.startGoalTime = startGoalTime;
		this.durationGoalTime = durationGoalTime;
		this.weeks = weeks;
		this.routineCategory = routineCategory;
		this.color = color;
		this.emoji = emoji;
	}
	
	public List<Week> getEnumWeeks(List<String> weeks) {
		List<Week> result = new ArrayList<>();
		for (String week : weeks) {
			result.add(Week.valueOf(week));
		}
		return result;
	}
	
	public List<RoutineCategory> getEnumRoutineCategory(List<String> routineCategory) {
		List<RoutineCategory> result = new ArrayList<>();
		for (String category : routineCategory) {
			result.add(RoutineCategory.valueOf(category));
		}
		return result;
	}
	
	public Routine toEntity(User user){
		return Routine.builder()
				.user(user)
				.name(this.name)
				.startGoalTime(this.startGoalTime)
				.durationGoalTime(this.durationGoalTime)
				.weeks(this.getEnumWeeks(this.getWeeks()))
				.routineCategory(this.getEnumRoutineCategory(this.getRoutineCategory()))
				.color(this.color)
				.emoji(this.emoji)
				.build();
	}
}
