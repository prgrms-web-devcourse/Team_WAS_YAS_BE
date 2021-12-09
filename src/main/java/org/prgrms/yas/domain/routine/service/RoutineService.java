package org.prgrms.yas.domain.routine.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineCreateResponse;
import org.prgrms.yas.domain.routine.dto.RoutineDeleteResponse;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateResponse;
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
                             .startTime(routineCreateRequest.getStartTime())
                             .durationTime(routineCreateRequest.getDurationTime())
                             .weeks(
                                 routineCreateRequest.getEnumWeeks(routineCreateRequest.getWeeks()))
                             .routineCategory(routineCreateRequest.getEnumRoutineCategory(
                                 routineCreateRequest.getRoutineCategory()))
                             .color(routineCreateRequest.getColor())
                             .emoji(routineCreateRequest.getEmoji())
                             .build();

    routineRepository.save(routine);
    RoutineCreateResponse routineCreateResponse = RoutineCreateResponse.builder()
                                                                       .name(routine.getName())
                                                                       .routineId(routine.getId())
                                                                       .startTime(
                                                                           routine.getStartTime())
                                                                       .durationTime(
                                                                           routine.getDurationTime())
                                                                       .weeks(
                                                                           routine.getStringWeeks(
                                                                               routine.getWeeks()))
                                                                       .routineCategory(
                                                                           routine.getStringCategory(
                                                                               routine.getRoutineCategory()))
                                                                       .color(routine.getColor())
                                                                       .emoji(routine.getEmoji())
                                                                       .build();
    return routineCreateResponse;
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
                                .startTime(routine.getStartTime())
                                .durationTime(routine.getDurationTime())
                                .weeks(routine.getStringWeeks(routine.getWeeks()))
                                .routineCategory(
                                    routine.getStringCategory(routine.getRoutineCategory()))
                                .color(routine.getColor())
                                .emoji(routine.getEmoji())
                                .build();
  }

  public void findId(Long routineId) throws NotFoundException {
    Routine routine = routineRepository.findById(routineId)
                                       .orElseThrow(NotFoundException::new);
  }

}
