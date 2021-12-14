package org.prgrms.yas.domain.mission.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionMissionStatusId {
	
	private Long missionId;
	private Long missionStatusId;
	
	@Builder
	public MissionMissionStatusId(Long missionId, Long missionStatusId) {
		this.missionId = missionId;
		this.missionStatusId = missionStatusId;
	}
}
