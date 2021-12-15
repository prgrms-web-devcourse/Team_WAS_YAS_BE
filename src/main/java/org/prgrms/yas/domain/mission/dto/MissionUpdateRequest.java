package org.prgrms.yas.domain.mission.dto;

import java.util.HashMap;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionUpdateRequest {
	
	private List<MissionOrder> missionOrders;
	
	@Builder
	public MissionUpdateRequest(
			List<MissionOrder> missionOrders
	) {
		this.missionOrders = missionOrders;
	}
}
