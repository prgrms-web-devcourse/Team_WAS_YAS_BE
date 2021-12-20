package org.prgrms.yas.domain.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionStatusDetailResponse {
	
	private int orders;
	private String startTime;
	private String endTime;
	private Long userDurationTime;
	
	@Builder
	public MissionStatusDetailResponse(
			int orders, LocalDateTime startTime, LocalDateTime endTime, Long userDurationTime
	) {
		this.orders = orders;
		this.startTime = startTime.plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		this.endTime = endTime.plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		this.userDurationTime = userDurationTime;
	}
}
