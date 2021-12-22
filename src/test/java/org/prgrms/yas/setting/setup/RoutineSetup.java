package org.prgrms.yas.setting.setup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
import org.prgrms.yas.domain.routine.domain.Week;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoutineSetup {
	
	@Autowired
	private UserSetup userSetup;
	@Autowired
	private RoutineRepository routineRepository;
	
	public Routine saveRoutine(User user) {
		List<Week> weeks = new ArrayList<>();
		weeks.add(Week.valueOf("MON"));
		weeks.add(Week.valueOf("TUE"));

		List<RoutineCategory> categories = new ArrayList<>();
		categories.add(RoutineCategory.valueOf("EXERCISE"));
		categories.add(RoutineCategory.valueOf("HEALTH"));

		Routine routine = Routine.builder()
		                         .user(user)
		                         .name("routineName")
		                         .startGoalTime(LocalDateTime.now())
		                         .durationGoalTime(1L)
		                         .weeks(weeks)
		                         .routineCategory(categories)
		                         .color("black")
		                         .emoji("^^")
		                         .build();
		return routineRepository.save(routine);
	}
}