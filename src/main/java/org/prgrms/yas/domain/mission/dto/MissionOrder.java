package org.prgrms.yas.domain.mission.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionOrder {
	
	private Long missionId;
	private int orders;
	
	@Builder
	public MissionOrder(Long missionId, int orders) {
		this.missionId = missionId;
		this.orders = orders;
	}
}
