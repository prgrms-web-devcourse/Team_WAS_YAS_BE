package org.prgrms.yas.domain.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Week;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeekDto {
	public static String weekToString(Week week){
		return week.getWeek();
	}
}
