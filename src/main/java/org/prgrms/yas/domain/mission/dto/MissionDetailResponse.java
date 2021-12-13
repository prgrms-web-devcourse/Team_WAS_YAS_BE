package org.prgrms.yas.domain.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionDetailResponse implements Comparable<MissionDetailResponse> {
	
	private Long missionId;
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
	private Long durationGoalTime;
	private int orders;
	private String emoji;
	private String color;
	
	@Builder
	public MissionDetailResponse(
			Long missionId, String name, Long durationGoalTime, int orders, String emoji, String color
	) {
		this.missionId = missionId;
		this.name = name;
		this.durationGoalTime = durationGoalTime;
		this.orders = orders;
		this.emoji = emoji;
		this.color = color;
	}
	
	@Override
	public int compareTo(MissionDetailResponse o) {
		return this.orders - o.orders;
	}
}

