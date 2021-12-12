package org.prgrms.yas.domain.routine.service;

import static java.util.stream.Collectors.toList;
import static org.prgrms.yas.global.error.ErrorCode.NOT_FOUND_RESOURCE_ERROR;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineCreateResponse;
import org.prgrms.yas.domain.routine.dto.RoutineDeleteResponse;
import org.prgrms.yas.domain.routine.dto.RoutineDetailResponse;
import org.prgrms.yas.domain.routine.dto.RoutineListResponse;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateResponse;
import org.prgrms.yas.domain.routine.exception.NotFoundRoutineException;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutineService {
	
	private final RoutineRepository routineRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public RoutineCreateResponse saveRoutine(Long userId, RoutineCreateRequest routineCreateRequest) {
		User user = userRepository.getById(userId);
		Routine routine = Routine.builder()
		                         .user(user)
		                         .name(routineCreateRequest.getName())
		                         .startGoalTime(routineCreateRequest.getStartGoalTime())
		                         .durationGoalTime(routineCreateRequest.getDurationGoalTime())
		                         .weeks(routineCreateRequest.getEnumWeeks(routineCreateRequest.getWeeks()))
		                         .routineCategory(routineCreateRequest.getEnumRoutineCategory(routineCreateRequest.getRoutineCategory()))
		                         .color(routineCreateRequest.getColor())
		                         .emoji(routineCreateRequest.getEmoji())
		                         .build();
		
		routineRepository.save(routine);
		return RoutineCreateResponse.builder()
		                            .name(routine.getName())
		                            .routineId(routine.getId())
		                            .startGoalTime(routine.getStartGoalTime())
		                            .durationGoalTime(routine.getDurationGoalTime())
		                            .weeks(routine.getStringWeeks(routine.getWeeks()))
		                            .routineCategory(routine.getStringCategory(routine.getRoutineCategory()))
		                            .color(routine.getColor())
		                            .emoji(routine.getEmoji())
		                            .build();
	}
	
	
	@Transactional
	public RoutineDeleteResponse deleteRoutine(Long routineId) throws NotFoundException {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(NotFoundException::new);
		routineRepository.deleteById(routineId);
		return RoutineDeleteResponse.builder()
		                            .routineId(routineId)
		                            .build();
		
	}
	
	@Transactional
	public RoutineUpdateResponse updateRoutine(
			Long routineId, RoutineUpdateRequest routineUpdateRequest
	) throws NotFoundException {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(NotFoundException::new);
		routine.updateRoutine(routineUpdateRequest.getEnumWeeks(routineUpdateRequest.getWeeks()));
		return RoutineUpdateResponse.builder()
		                            .name(routine.getName())
		                            .routineId(routine.getId())
		                            .startGoalTime(routine.getStartGoalTime())
		                            .durationGoalTime(routine.getDurationGoalTime())
		                            .weeks(routine.getStringWeeks(routine.getWeeks()))
		                            .routineCategory(routine.getStringCategory(routine.getRoutineCategory()))
		                            .color(routine.getColor())
		                            .emoji(routine.getEmoji())
		                            .build();
	}
	
	@Transactional
	public void findId(Long routineId) throws NotFoundException {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(NotFoundException::new);
	}
	
	@Transactional
	public List<RoutineListResponse> findRoutines(Long userId) throws NotFoundException {
		User user = userRepository.findById(userId)
		                          .orElseThrow(NotFoundException::new);
		List<Routine> routines = routineRepository.getByUser(user);
		return routines.stream()
		               .map(Routine::toRoutineListResponse)
		               .collect(toList());
	}
	
	@Transactional
	public RoutineDetailResponse findMissions(Long routineId) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
		
		return routine.toRoutineDetailResponse();
	}
}
