package org.prgrms.yas.domain.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionStatusDetailResponse {
	
	private int orders;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private ZonedDateTime startTime;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private ZonedDateTime endTime;
	private Long userDurationTime;
	
	@Builder
	public MissionStatusDetailResponse(
			int orders, ZonedDateTime startTime, ZonedDateTime endTime, Long userDurationTime
	) {
		this.orders = orders;
		this.startTime = startTime;
		this.endTime = endTime;
		this.userDurationTime = userDurationTime;
	}
}
