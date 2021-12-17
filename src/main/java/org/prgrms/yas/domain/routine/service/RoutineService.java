package org.prgrms.yas.domain.routine.service;

import static java.util.stream.Collectors.toList;
import static org.prgrms.yas.domain.post.global.error.ErrorCode.NOT_FOUND_RESOURCE_ERROR;

import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.Week;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
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

@Service
@RequiredArgsConstructor
public class RoutineService {
	
	private final RoutineRepository routineRepository;
	private final UserRepository userRepository;
	private static final double IS_NOT_WEEK = 0;
	
	@Transactional
	public Long saveRoutine(Long userId, RoutineCreateRequest routineCreateRequest) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
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
		
		return routineRepository.save(routine)
		                        .getId();
	}
	
	
	@Transactional
	public Long deleteRoutine(Long routineId) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
		routineRepository.deleteById(routineId);
		return routineId;
		
	}
	
	@Transactional
	public RoutineUpdateResponse updateRoutine(
			Long routineId, RoutineUpdateRequest routineUpdateRequest
	) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
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
	public void findId(Long routineId) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
	}
	
	
	@Transactional
	public RoutineDetailResponse findMissions(Long routineId) {
		Routine routine = routineRepository.findById(routineId)
		                                   .orElseThrow(() -> new NotFoundRoutineException(NOT_FOUND_RESOURCE_ERROR));
		
		return routine.toRoutineDetailResponse();
	}
	
	@Transactional
	public List<RoutineListResponse> findRoutines(Long userId) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
		List<Routine> routines = routineRepository.getByUser(user);
		return routines.stream()
		               .map(Routine::toRoutineListResponse)
		               .collect(toList());
	}
	
	@Transactional
	public List<RoutineListResponse> findFinishRoutines(Long userId, String status) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new NotFoundUserException(NOT_FOUND_RESOURCE_ERROR));
		
		List<Routine> routines = routineRepository.getByUser(user);
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
}
