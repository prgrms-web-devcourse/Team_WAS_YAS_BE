package org.prgrms.yas.domain.routine.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.mission.dto.MissionDetailResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineDetailResponse {
	
	private String name;
	private String emoji;
	private String color;
	private List<String> routineCategory;
	private List<String> weeks;
	private List<MissionDetailResponse> missionDetailResponses;
	
	@Builder
	public RoutineDetailResponse(
			String name, String emoji, String color, List<String> routineCategory, List<String> weeks,
			List<MissionDetailResponse> missionDetailResponses
	) {
		this.name = name;
		this.emoji = emoji;
		this.color = color;
		this.routineCategory = routineCategory;
		this.missionDetailResponses = missionDetailResponses;
		this.weeks = weeks;
	}
}
