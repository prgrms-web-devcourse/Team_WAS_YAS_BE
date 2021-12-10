package org.prgrms.yas.domain.mission.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionCreateResponse {
	
	private Long missionId;
	private String emoji;
	private String name;
	private Long durationGoalTime;
	
	@Builder
	public MissionCreateResponse(
			Long missionId, String emoji, String name, Long durationGoalTime
	) {
		this.missionId = missionId;
		this.emoji = emoji;
		this.name = name;
		this.durationGoalTime = durationGoalTime;
	}
}
