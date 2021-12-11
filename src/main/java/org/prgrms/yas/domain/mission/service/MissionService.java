package org.prgrms.yas.domain.mission.service;

import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.dto.MissionCreateRequest;
import org.prgrms.yas.domain.mission.dto.MissionCreateResponse;
import org.prgrms.yas.domain.mission.exception.NotFoundMissionException;
import org.prgrms.yas.domain.mission.repository.MissionRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.routine.service.RoutineService;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {
	
	private final RoutineRepository routineRepository;
	private final MissionRepository missionRepository;
	
	public MissionCreateResponse saveMission(
			Long routineId, MissionCreateRequest missionCreateRequest
	) throws NotFoundException {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundMissionException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		Mission mission = Mission.builder()
		                         .name(missionCreateRequest.getName())
		                         .durationGoalTime(missionCreateRequest.getDurationGoalTime())
		                         .emoji(missionCreateRequest.getEmoji())
		                         .orders(missionCreateRequest.getOrders())
		                         .routine(routine)
		                         .name(missionCreateRequest.getName())
		                         .build();
		
		missionRepository.save(mission);
		
		return mission.toMissionCreateResponse();
		
	}
}
