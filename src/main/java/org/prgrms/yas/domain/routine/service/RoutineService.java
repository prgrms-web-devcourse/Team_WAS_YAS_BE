package org.prgrms.yas.domain.routine.service;

import static java.util.stream.Collectors.toList;
import static org.prgrms.yas.global.error.ErrorCode.NOT_FOUND_RESOURCE_ERROR;

import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.mission.domain.Mission;
import org.prgrms.yas.domain.mission.repository.MissionRepository;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.Week;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineDetailCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineListResponse;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateResponse;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineService {
	
	private static final double IS_NOT_WEEK = 0;
	
	private final RoutineRepository routineRepository;
	private final UserRepository userRepository;
	private final MissionRepository missionRepository;
	
	@Transactional
	public Long saveRoutine(Long userId, RoutineCreateRequest routineCreateRequest) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
		Routine routine = routineCreateRequest.toEntity(user);
		
		return routineRepository.save(routine)
		                        .getId();
	}
	
	@Transactional
	public Long deleteRoutine(Long routineId) {
		Routine routine = routineRepository.findByIdAndIsDeletedFalse(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
		routine.deleteRoutine();
		
		return routineId;
	}
	
	@Transactional
	public RoutineUpdateResponse updateRoutine(
			Long routineId, RoutineUpdateRequest routineUpdateRequest
	) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
		routine.updateRoutine(routineUpdateRequest.getEnumWeeks(routineUpdateRequest.getWeeks()));
		
		return routine.toRoutineUpdateResponse();
	}
	
	@Transactional(readOnly = true)
	public void findId(Long routineId) {
		Routine routine = routineRepository.findByIdAndIsDeletedFalse(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
	}
	
	
	@Transactional(readOnly = true)
	public RoutineDetailResponse findMissions(Long routineId) {
		Routine routine = routineRepository.findByIdAndIsDeletedFalse(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
		
		List<Mission> missions = missionRepository.getByRoutineAndIsDeletedFalse(routine);
		return routine.toRoutineDetailResponse(missions);
	}
	
	@Transactional(readOnly = true)
	public List<RoutineListResponse> findRoutines(Long userId) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
		List<Routine> routines = routineRepository.getByUserAndIsDeletedFalse(user);
		return routines.stream()
		               .map(Routine::toRoutineListResponse)
		               .collect(toList());
	}
	
	@Transactional(readOnly = true)
	public List<RoutineListResponse> findFinishRoutines(Long userId, String status) {
		User user = userRepository.findByIdAndIsDeletedFalse(userId)
		                          .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
		
		List<Routine> routines = routineRepository.getByUserAndIsDeletedFalse(user);
		Calendar calendar = Calendar.getInstance();
		
		Predicate<Week> isWeek = week -> (week.ordinal() + 1 == calendar.get(Calendar.DAY_OF_WEEK));
		List<Routine> weekRoutine = routines.stream()
		                                    .filter(routine -> {
					
					                                    long cnt = routine.getWeeks()
					                                                      .stream()
					                                                      .filter(isWeek)
					                                                      .count();
					
					                                    return cnt != IS_NOT_WEEK;
				                                    }
		
		                                    )
		                                    .collect(toList());
		
		Status statusEnum = Status.from(status);
		List<Routine> findRoutines = statusEnum.apply(weekRoutine);
		
		return findRoutines.stream()
		                   .map(Routine::toRoutineListResponse)
		                   .collect(Collectors.toList());
	}
	
	@Transactional
	public Long saveRoutineFromPost(
			Long userId, RoutineDetailCreateRequest routineDetailCreateRequest
	) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
		
		Routine routine = routineDetailCreateRequest.toEntity(user);
		
		routineDetailCreateRequest.getMissionCreateRequest()
		                          .stream()
		                          .map(missionCreateRequest -> missionCreateRequest.toEntity(routine))
		                          .forEach(missionRepository::save);
		
		return routineRepository.save(routine)
		                        .getId();
	}
}
