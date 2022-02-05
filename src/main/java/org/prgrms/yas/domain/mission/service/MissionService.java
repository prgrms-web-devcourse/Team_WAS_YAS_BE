package org.prgrms.yas.domain.mission.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.dto.MissionCreateRequest;
import org.prgrms.yas.domain.mission.dto.MissionDetailResponse;
import org.prgrms.yas.domain.mission.dto.MissionOrder;
import org.prgrms.yas.domain.mission.dto.MissionUpdateRequest;
import org.prgrms.yas.domain.mission.exception.NotFoundMissionException;
import org.prgrms.yas.domain.mission.repository.MissionRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {
	
	private final RoutineRepository routineRepository;
	private final MissionRepository missionRepository;
	
	@Transactional
	public Long saveMission(
			Long routineId, MissionCreateRequest missionCreateRequest
	) {
		Routine routine = routineRepository.findByIdAndIsDeletedFalse(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		routine.addDurationGoalTime(missionCreateRequest.getDurationGoalTime());
		return missionRepository.save(missionCreateRequest.toEntity(routine))
		                        .getId();
		
	}
	
	@Transactional
	public Long deleteMission(Long routineId, Long missionId) {
		Routine routine = routineRepository.findByIdAndIsDeletedFalse(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		Mission mission = missionRepository.findById(missionId)
		                                   .orElseThrow(() -> new NotFoundMissionException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		routine.minusDurationGoalTime(mission.getDurationGoalTime());
		mission.deleteMission();
		
		return missionId;
	}
	
	@Transactional
	public List<MissionDetailResponse> updateMission(
			Long routineId, MissionUpdateRequest missionUpdateRequest
	) {
		List<Mission> missions = missionRepository.findByRoutineId(routineId)
		                                          .orElseThrow(() -> new NotFoundMissionException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		for (MissionOrder missionOrder : missionUpdateRequest.getMissionOrders()) {
			missionRepository.getById(missionOrder.getMissionId())
			                 .updateOrders(missionOrder.getOrders());
		}
		
		return missions.stream()
		               .map(Mission::toMissionDetailResponse)
		               .collect(Collectors.toList());
	}
}
