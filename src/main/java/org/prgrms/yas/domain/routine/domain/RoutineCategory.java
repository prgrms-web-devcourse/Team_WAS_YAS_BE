package org.prgrms.yas.domain.routine.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoutineCategory {
	EXERCISE("EXERCISE"),
	HEALTH("HEALTH"),
	FOOD("FOOD"),
	GAME("GAME"),
	HOBBY("HOBBY"),
	STUDY("STUDY"),
	TOTAL("TOTAL"),
	SHOPPING("SHOPPING"),
	LIFE("LIFE"),
	ART("ART"),
	MUSIC("MUSIC");
	
	private String routineCategory;
	
	RoutineCategory(String routineCategory) {
		this.routineCategory = routineCategory;
	}
}
