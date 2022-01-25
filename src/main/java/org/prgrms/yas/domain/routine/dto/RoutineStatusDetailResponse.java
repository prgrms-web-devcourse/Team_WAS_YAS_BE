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
	private int emoji;
	private RoutineDetailResponse RoutineDetailResponse;
	private List<RoutineStatusImageDto> routineStatusImage = new ArrayList<>();
	
	@Builder
	public RoutineStatusDetailResponse(
			Long routineStatusId, ZonedDateTime dateTime, String content, int emoji,
			org.prgrms.yas.domain.routine.dto.RoutineDetailResponse routineDetailResponse,
			List<RoutineStatusImageDto> routineStatusImage
	) {
		this.routineStatusId = routineStatusId;
		this.dateTime = dateTime;
		this.content = content;
		this.emoji = emoji;
		RoutineDetailResponse = routineDetailResponse;
		this.routineStatusImage = routineStatusImage;
	}
}
