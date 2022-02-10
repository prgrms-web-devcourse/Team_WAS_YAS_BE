package org.prgrms.yas.domain.routine.dto;

import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineStatusListResponse {
	
	private Long routineStatusId;
	private ZonedDateTime startTime;
	private RoutineListResponse routineListResponse;
	
	@Builder
	public RoutineStatusListResponse(
			Long routineStatusId, ZonedDateTime startTime, RoutineListResponse routineListResponse
	) {
		this.routineStatusId = routineStatusId;
		this.startTime = startTime;
		this.routineListResponse = routineListResponse;
	}
}