package org.prgrms.yas.domain.routine.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;


public enum Status {
	FINISH(
			"finish",
			routines -> {
				return routines.stream()
				               .filter(routine -> {
					               Predicate<RoutineStatus> isDate = routineStatus -> (routineStatus.getDate()
					                                                                                .isEqual(LocalDate.now()));
					               long num = routine.getRoutineStatuses()
					                                 .stream()
					                                 .filter(isDate)
					                                 .count();
					               return num != 0;
				               })
				               .collect(toList());
			}
	),
	
	NOT_FINISH(
			"not_finish",
			routines -> {
				return routines.stream()
				               .filter(routine -> {
					               Predicate<RoutineStatus> isDate = routineStatus -> (routineStatus.getDate()
					                                                                                .isEqual(LocalDate.now()));
					               long num = routine.getRoutineStatuses()
					                                 .stream()
					                                 .filter(isDate)
					                                 .count();
					               return num == 0;
				               })
				               .collect(toList());
			}
	);
	
	private String value;
	
	public String value() {
		return value;
	}
	
	private final Function<List<Routine>, List<Routine>> function;
	
	Status(String value, Function<List<Routine>, List<Routine>> function) {
		this.value = value;
		this.function = function;
	}
	
	public static Status from(String value) {
		return Status.valueOf(value.toUpperCase());
	}
	
	public List<Routine> apply(List<Routine> routines) {
		return this.function.apply(routines);
	}
}
