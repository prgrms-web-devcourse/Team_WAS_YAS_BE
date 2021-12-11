package org.prgrms.yas.domain.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionCreateRequest {
	
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
	private Long durationGoalTime;
	private int orders;
	private String emoji;
	private String color;
	
	@Builder
	public MissionCreateRequest(String name, Long durationGoalTime, int orders, String emoji,String color) {
		this.name = name;
		this.durationGoalTime = durationGoalTime;
		this.orders = orders;
		this.emoji = emoji;
		this.color = color;
	}
}
