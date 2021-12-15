package org.prgrms.yas.domain.mission.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionStatusCreateResponse {
	
	List<MissionMissionStatusId> missionMissionStatusIds;
	Long routineStatusId;
	
	@Builder
	public MissionStatusCreateResponse(
			List<MissionMissionStatusId> missionMissionStatusIds, Long routineStatusId
	) {
		this.missionMissionStatusIds = missionMissionStatusIds;
		this.routineStatusId = routineStatusId;
	}
}
