package org.prgrms.yas.domain.mission.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.domain.MissionStatus;
import org.prgrms.yas.domain.mission.dto.MissionDetailStatusResponse;
import org.prgrms.yas.domain.mission.dto.MissionMissionStatusId;
import org.prgrms.yas.domain.mission.dto.MissionStatusCreateResponse;
import org.prgrms.yas.domain.mission.dto.MissionStatusUpdateRequest;
import org.prgrms.yas.domain.mission.exception.NotFoundMissionStatusException;
import org.prgrms.yas.domain.mission.repository.MissionStatusRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineStatus;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineStatusException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.routine.repository.RoutineStatusRepository;
import org.prgrms.yas.domain.routine.service.RoutineStatusService;
import org.prgrms.yas.global.error.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionStatusService {
	
	private final MissionStatusRepository missionStatusRepository;
	private final RoutineStatusRepository routineStatusRepository;
	private final RoutineStatusService routineStatusService;
	private final RoutineRepository routineRepository;
	
	@Transactional
	public MissionStatusCreateResponse saveMissionStatus(
			Long routineId
	) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		List<MissionMissionStatusId> missionMissionStatusIds = new ArrayList<>();
		
		//routineStatus ????????? ??????
		RoutineStatus routineStatus = RoutineStatus.builder()
		                                           .routine(routine)
		                                           .dateTime(LocalDateTime.now()
		                                                                  .plusHours(9))
		                                           .content("")
		                                           .emotion(1)
		                                           .build();
		Long routineStatusId = routineStatusRepository.save(routineStatus)
		                                              .getId();
		
		//????????? ???????????? ?????? Status ????????? ??????
		for (Mission mission : routine.getMissions()) {
			MissionStatus missionStatus = missionStatusRepository.save(MissionStatus.builder()
			                                                                        .dateTime(LocalDateTime.now()
			                                                                                               .plusHours(9))
			                                                                        .mission(mission)
			                                                                        .build());
			
			MissionMissionStatusId missionMissionStatusId = new MissionMissionStatusId(
					mission.getId(),
					missionStatus.getId()
			);
			missionMissionStatusIds.add(missionMissionStatusId);
		}
		return MissionStatusCreateResponse.builder()
		                                  .routineStatusId(routineStatusId)
		                                  .missionMissionStatusIds(missionMissionStatusIds)
		                                  .build();
	}
	
	@Transactional
	public Long updateMissionStatus(
			Long routineId, MissionStatusUpdateRequest missionStatusUpdateRequest
	) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		RoutineStatus routineStatus = routineStatusRepository.findById(missionStatusUpdateRequest.getRoutineStatusId())
		                                                     .orElseThrow(() -> new NotFoundRoutineStatusException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		Long savedUserDurationTime = 0L;
		//?????? ???????????? ?????? ????????? ?????? ??????
		if (missionStatusUpdateRequest.getOrders() == 0
				&& missionStatusUpdateRequest.getStartTime() != null) {
			if (routineStatus.getEndTime() != null) {
				routineStatus.setEndTimeIsNull();
			}
			routineStatus.setStartTime(missionStatusUpdateRequest.getStartTime());
		}
		
		//????????? ????????? ????????? ???????????? ??????????????? endTime ??????
		if (missionStatusUpdateRequest.getOrders() == routine.getMissions()
		                                                     .size() - 1
				&& missionStatusUpdateRequest.getEndTime() != null) {
			routineStatus.setEndTime(missionStatusUpdateRequest.getEndTime());
			routineStatus.setUserDurationTime(savedUserDurationTime);
		}
		
		//MissionStatus ??????????????? ????????? ?????? ????????????
		MissionStatus missionStatus = missionStatusRepository.findById(missionStatusUpdateRequest.getMissionStatusId())
		                                                     .orElseThrow(() -> new NotFoundMissionStatusException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		if (missionStatusUpdateRequest.getStartTime() != null) {
			if (missionStatus.getEndTime() != null) {
				savedUserDurationTime -= missionStatusUpdateRequest.getUserDurationTime();
				missionStatus.updateEndTimeIsNull();
			}
			if (routineStatus.getEndTime() != null) {
				routineStatus.setEndTimeIsNull();
			}
			
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
		savedUserDurationTime += missionStatusUpdateRequest.getUserDurationTime();
		return missionStatusRepository.save(missionStatus)
		                              .getId();
	}
	
	@Transactional
	public List<MissionDetailStatusResponse> getMissionStatuses(Long routineId) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		List<MissionDetailStatusResponse> result = new ArrayList<>();
		
		RoutineStatus routineStatus = routineStatusService.findRoutineStatus(
				routineId,
				LocalDate.now()
				         .toString()
		);
		
		//MissionStatus ??? ?????? ????????? ?????? ???????????? ?????????
		Predicate<MissionStatus> reservationPredicateCheckOut = missionStatus -> (missionStatus.getDateTime()
		                                                                                       .toLocalDate()
		                                                                                       .isEqual(LocalDateTime.now()
		                                                                                                             .plusHours(9)
		                                                                                                             .toLocalDate()));
		
		for (Mission missions : routine.getMissions()) {
			List<MissionStatus> missionStatuses = missionStatusRepository.getByMission(missions)
			                                                             .stream()
			                                                             .filter(reservationPredicateCheckOut)
			                                                             .collect(Collectors.toList());
			
			for (MissionStatus missionStatus : missionStatuses) {
				result.add(missions.toMissionDetailStatusResponse(
						missionStatus,
						routineStatus.getId()
				));
			}
		}
		return result;
	}
	
}
