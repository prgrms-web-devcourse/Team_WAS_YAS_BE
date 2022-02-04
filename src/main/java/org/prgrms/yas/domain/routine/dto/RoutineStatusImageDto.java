package org.prgrms.yas.domain.routine.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineStatusImageDto {
	
	private Long routineStatusImageId;
	private String imageUrl;
	
	@Builder
	public RoutineStatusImageDto(Long routineStatusImageId, String imageUrl) {
		this.routineStatusImageId = routineStatusImageId;
		this.imageUrl = imageUrl;
	}
}
