package org.prgrms.yas.domain.routine.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineStatusDetailResponse {
	
	private Long routineStatusId;
	private ZonedDateTime dateTime;
	private String content;
	private int emotion;
	private RoutineDetailResponse RoutineDetailResponse;
	private List<RoutineStatusImageDto> routineStatusImage = new ArrayList<>();
	
	@Builder
	public RoutineStatusDetailResponse(
			Long routineStatusId, ZonedDateTime dateTime, String content, int emotion,
			org.prgrms.yas.domain.routine.dto.RoutineDetailResponse routineDetailResponse,
			List<RoutineStatusImageDto> routineStatusImage
	) {
		this.routineStatusId = routineStatusId;
		this.dateTime = dateTime;
		this.content = content;
		this.emotion = emotion;
		RoutineDetailResponse = routineDetailResponse;
		this.routineStatusImage = routineStatusImage;
	}
}
