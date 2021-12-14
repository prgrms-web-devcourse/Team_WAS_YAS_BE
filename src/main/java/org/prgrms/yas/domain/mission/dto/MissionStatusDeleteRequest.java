package org.prgrms.yas.domain.mission.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionStatusDeleteRequest {
	
	private Long missionStatusId;
	
	@Builder
	public MissionStatusDeleteRequest(Long missionStatusId) {
		this.missionStatusId = missionStatusId;
	}
}
