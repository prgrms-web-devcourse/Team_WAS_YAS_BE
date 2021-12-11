package org.prgrms.yas.domain.routine.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Week {
	MON("MON"),
	TUE("TUE"),
	WED("WED"),
	THU("THU"),
	FRI("FRI"),
	SAT("SAT"),
	SUN("SUN");
	
	private String week;
	
	Week(String week) {
		this.week = week;
	}
}
