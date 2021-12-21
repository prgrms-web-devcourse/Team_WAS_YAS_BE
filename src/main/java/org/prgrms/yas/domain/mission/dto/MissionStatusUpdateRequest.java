package org.prgrms.yas.domain.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionStatusUpdateRequest {
	
	private Long routineStatusId;
	private Long missionStatusId;
	private int orders;
	private Long userDurationTime;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",timezone = "Asia/Seoul")
	private LocalDateTime endTime;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",timezone = "Asia/Seoul")
	private LocalDateTime startTime;
	
	@Builder
	public MissionStatusUpdateRequest(
			Long missionStatusId, int orders, Long userDurationTime, LocalDateTime endTime,
			LocalDateTime startTime, Long routineStatusId
	) {
		this.missionStatusId = missionStatusId;
		this.orders = orders;
		this.userDurationTime = userDurationTime;
		this.endTime = endTime;
		this.startTime = startTime;
		this.routineStatusId = routineStatusId;
	}
}
