package org.prgrms.yas.domain.mission.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionStatusDetailResponse {
	
	private int orders;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	@Builder
	public MissionStatusDetailResponse(
			int orders, LocalDateTime startTime, LocalDateTime endTime
	) {
		this.orders = orders;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
