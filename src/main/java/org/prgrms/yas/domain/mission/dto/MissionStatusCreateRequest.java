package org.prgrms.yas.domain.mission.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.domain.MissionStatus;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MissionStatusCreateRequest {
	
	private int orders;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime startTime;
	
	@Builder
	public MissionStatusCreateRequest(int orders, LocalDateTime startTime) {
		this.orders = orders;
		this.startTime = startTime;
	}
	
	public MissionStatus toEntity(Mission mission) {
		return MissionStatus.builder()
		                    .mission(mission)
		                    .orders(orders)
		                    .startTime(startTime)
		                    .build();
	}
}
