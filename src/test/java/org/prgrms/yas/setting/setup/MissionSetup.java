package org.prgrms.yas.setting.setup;

import java.time.LocalDateTime;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.repository.MissionRepository;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MissionSetup {
	
	@Autowired
	private MissionRepository missionRepository;
	
	public Mission saveMission(Routine routine) {
		Mission mission = Mission.builder()
		                         .name("mission_name")
		                         .durationGoalTime(1L)
		                         .orders(1)
		                         .emoji("^^")
		                         .routine(routine)
		                         .color("black")
		                         .build();
		return missionRepository.save(mission);
	}
}