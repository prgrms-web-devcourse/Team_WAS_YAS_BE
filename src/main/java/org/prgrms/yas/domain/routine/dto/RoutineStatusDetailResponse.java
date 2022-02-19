package org.prgrms.yas.domain.routine.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.mission.dto.MissionDetailStatusResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineStatusDetailResponse {
	
	private Long routineStatusId;
	private ZonedDateTime startTime;
	private String content;
	private int emotion;
	private RoutineDto routineDto;
	private List<RoutineStatusImageDto> routineStatusImage = new ArrayList<>();
	private List<MissionDetailStatusResponse> missionDetailStatusResponses = new ArrayList<>();
	
	@Builder
	public RoutineStatusDetailResponse(
			Long routineStatusId, ZonedDateTime startTime, String content, int emotion,
			org.prgrms.yas.domain.routine.dto.RoutineDto routineDto,
			List<RoutineStatusImageDto> routineStatusImage,
			List<MissionDetailStatusResponse> missionDetailStatusResponses
	) {
		this.routineStatusId = routineStatusId;
		this.startTime = startTime;
		this.content = content;
		this.emotion = emotion;
		this.routineDto = routineDto;
		this.routineStatusImage = routineStatusImage;
		this.missionDetailStatusResponses = missionDetailStatusResponses;
	}
}
