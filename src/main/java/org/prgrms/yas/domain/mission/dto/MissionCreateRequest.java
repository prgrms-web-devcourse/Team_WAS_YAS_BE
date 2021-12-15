package org.prgrms.yas.domain.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.routine.domain.Routine;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionCreateRequest {
	
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Long durationGoalTime;
	private int orders;
	private String emoji;
	private String color;
	
	@Builder
	public MissionCreateRequest(
			String name, Long durationGoalTime, int orders, String emoji, String color
	) {
		this.name = name;
		this.durationGoalTime = durationGoalTime;
		this.orders = orders;
		this.emoji = emoji;
		this.color = color;
	}
	
	public Mission toEntity(Routine routine) {
		return Mission.builder()
		              .name(name)
		              .durationGoalTime(durationGoalTime)
		              .emoji(emoji)
		              .orders(orders)
		              .routine(routine)
		              .name(name)
		              .build();
	}
}
