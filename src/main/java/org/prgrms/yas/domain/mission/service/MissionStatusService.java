package org.prgrms.yas.domain.mission.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.domain.MissionStatus;
import org.prgrms.yas.domain.mission.dto.MissionStatusCreateRequest;
import org.prgrms.yas.domain.mission.dto.MissionStatusUpdateRequest;
import org.prgrms.yas.domain.mission.exception.NotFoundMissionException;
import org.prgrms.yas.domain.mission.exception.NotFoundMissionStatusException;
import org.prgrms.yas.domain.mission.repository.MissionRepository;
import org.prgrms.yas.domain.mission.repository.MissionStatusRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.prgrms.yas.domain.routine.repository.RoutineStatusRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionStatusService {
	
	private final MissionStatusRepository missionStatusRepository;
	private final RoutineStatusRepository routineStatusRepository;
	private final MissionRepository missionRepository;
	
	@Transactional
	public Long saveMissionStatus(
			Long missionId, MissionStatusCreateRequest missionStatusCreateRequest
	) {
		Mission mission = missionRepository.findById(missionId)
		                                   .orElseThrow(() -> new NotFoundMissionException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		log.info("결과 missionStatusCreateRequest 뭐니 ?? ::{}",
				missionStatusCreateRequest.getStartTime());
		
		//첫번째 미션 시작할때 루틴진행 테이블에도 시작 데이터 저장
		if (missionStatusCreateRequest.getOrders() == 1) {
			Routine routine = mission.getRoutine();
			RoutineStatus routineStatus = RoutineStatus.builder()
			                                           .startTime(missionStatusCreateRequest.getStartTime())
			                                           .routine(routine)
			                                           .build();
			routineStatusRepository.save(routineStatus);
		}
		
		MissionStatus missionStatus = missionStatusCreateRequest.toEntity(mission);
		return missionStatusRepository.save(missionStatus)
		                              .getId();
		
	}
	
	@Transactional
	public Long updateMissionStatus(
			Long missionId, MissionStatusUpdateRequest missionStatusUpdateRequest
	) {
		Mission mission = missionRepository.findById(missionId)
		                                   .orElseThrow(() -> new NotFoundMissionException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		MissionStatus missionStatus = missionStatusRepository.findById(missionStatusUpdateRequest.getMissionStatusId())
		                                                     .orElseThrow(() -> new NotFoundMissionStatusException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		//마지막 미션 끝날때 루틴진행 테이블도 같이 업데이트
		Routine routine = mission.getRoutine();
		if (missionStatusUpdateRequest.getOrders() == routine.getMissions()
		                                                     .size()) {
			RoutineStatus routineStatus = RoutineStatus.builder()
			                                           .endTime(missionStatusUpdateRequest.getEndTime())
			                                           .build();
			routineStatusRepository.save(routineStatus);
		}
		
		missionStatus.updateMissionStatus(
				missionStatusUpdateRequest.getOrders(),
				missionStatusUpdateRequest.getUserDurationTime(),
				missionStatusUpdateRequest.getEndTime()
		);
		return missionStatusRepository.save(missionStatus)
		                              .getId();
	}
}
