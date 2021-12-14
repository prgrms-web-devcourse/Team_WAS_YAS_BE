package org.prgrms.yas.domain.mission.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.domain.MissionStatus;
import org.prgrms.yas.domain.mission.dto.MissionDetailResponse;
import org.prgrms.yas.domain.mission.dto.MissionMissionStatusId;
import org.prgrms.yas.domain.mission.dto.MissionStatusCreateResponse;
import org.prgrms.yas.domain.mission.dto.MissionStatusUpdateRequest;
import org.prgrms.yas.domain.mission.exception.NotFoundMissionException;
import org.prgrms.yas.domain.mission.exception.NotFoundMissionStatusException;
import org.prgrms.yas.domain.mission.repository.MissionRepository;
import org.prgrms.yas.domain.mission.repository.MissionStatusRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.routine.repository.RoutineStatusRepository;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionStatusService {
	
	private final MissionStatusRepository missionStatusRepository;
	private final RoutineStatusRepository routineStatusRepository;
	private final RoutineRepository routineRepository;
	private final MissionRepository missionRepository;
	
	@Transactional
	public MissionStatusCreateResponse saveMissionStatus(
			Long routineId
	) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		List<MissionMissionStatusId> missionMissionStatusIds = new ArrayList<>();
		
		for (Mission mission : routine.getMissions()) {
			MissionStatus missionStatus = missionStatusRepository.save(MissionStatus.builder()
			                                                                        .mission(mission)
			                                                                        .build());
			
			MissionMissionStatusId missionMissionStatusId = new MissionMissionStatusId(
					mission.getId(),
					missionStatus.getId()
			);
			missionMissionStatusIds.add(missionMissionStatusId);
		}
		return MissionStatusCreateResponse.builder()
		                                  .missionMissionStatusIds(missionMissionStatusIds)
		                                  .build();
	}
	
	@Transactional
	public Long updateMissionStatus(
			Long routineId, MissionStatusUpdateRequest missionStatusUpdateRequest
	) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		//첫번째 미션 시작할때 루틴진행 테이블에도 startTime 저장
		if (missionStatusUpdateRequest.getOrders() == 1) {
			RoutineStatus routineStatus = RoutineStatus.builder()
			                                           .startTime(missionStatusUpdateRequest.getStartTime())
			                                           .routine(routine)
			                                           .build();
			routineStatusRepository.save(routineStatus);
		}
		
		//마지막 미션이 끝날때 루틴진행 테이블에더 endTime 저장
		if (missionStatusUpdateRequest.getOrders() == routine.getMissions()
		                                                     .size()) {
			RoutineStatus routineStatus = RoutineStatus.builder()
			                                           .endTime(missionStatusUpdateRequest.getEndTime())
			                                           .build();
			routineStatusRepository.save(routineStatus);
		}
		
		//MissionStatus 시작할때랑 끝날때 정보 업데이트
		MissionStatus missionStatus = missionStatusRepository.findById(missionStatusUpdateRequest.getMissionStatusId())
		                                                     .orElseThrow(() -> new NotFoundMissionStatusException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		if (missionStatusUpdateRequest.getStartTime() != null) {
			missionStatus.updateStartTime(
					missionStatusUpdateRequest.getOrders(),
					missionStatusUpdateRequest.getStartTime()
			);
			return missionStatusRepository.save(missionStatus)
			                              .getId();
		}
		
		missionStatus.updateEndTime(
				missionStatusUpdateRequest.getOrders(),
				missionStatusUpdateRequest.getUserDurationTime(),
				missionStatusUpdateRequest.getEndTime()
		);
		return missionStatusRepository.save(missionStatus)
		                              .getId();
	}
	
	@Transactional
	public List<MissionDetailResponse> getMissionStatuses(Long missionId) {
		Mission mission = missionRepository.findById(missionId)
		                                   .orElseThrow(() -> new NotFoundMissionException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		Routine routine = mission.getRoutine();
		List<MissionDetailResponse> result = new ArrayList<>();
		
		//MissionStatus 중 오늘 날짜에 맞는 데이터만 가져옴
		Predicate<MissionStatus> reservationPredicateCheckOut = missionStatus -> (missionStatus.getStartTime()
		                                                                                       .toLocalDate()
		                                                                                       .isEqual(LocalDate.now()));
		
		for (Mission missions : mission.getRoutine()
		                               .getMissions()) {
			List<MissionStatus> missionStatuses = missionStatusRepository.getByMission(missions)
			                                                             .stream()
			                                                             .filter(reservationPredicateCheckOut)
			                                                             .collect(Collectors.toList());
			
			for (MissionStatus missionStatus : missionStatuses) {
				result.add(missions.toMissionDetailResponse(missionStatus));
			}
		}
		return result;
	}
	
}
