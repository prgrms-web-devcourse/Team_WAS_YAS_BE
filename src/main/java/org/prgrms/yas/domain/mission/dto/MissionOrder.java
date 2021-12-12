package org.prgrms.yas.domain.mission.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionOrder {
	
	private Long id;
	private int orders;
	
	@Builder
	public MissionOrder(Long id, int orders) {
		this.id = id;
		this.orders = orders;
	}
}
