package org.prgrms.yas.domain.post.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.mission.domain.Mission;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionDto {
	private Long missionId;
	private String name;
	private Long durationGoalTime;
	private String emoji;
	private String color;
	
	@Builder
	public MissionDto(Mission mission) {
		this.missionId = mission.getId();
		this.name = mission.getName();
		this.durationGoalTime = mission.getDurationGoalTime();
		this.emoji = mission.getEmoji();
		this.color = mission.getColor();
	}
}

