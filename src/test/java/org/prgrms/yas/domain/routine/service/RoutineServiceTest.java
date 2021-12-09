package org.prgrms.yas.domain.routine.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineCreateResponse;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Rollback(value = false)
class RoutineServiceTest {

  @Autowired
  private RoutineService routineService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoutineRepository routineRepository;

  Long findId;

  @BeforeEach
  void setting() {

    User user = User.builder()
                    .name("oni")
                    .nickname("oni")
                    .email("kcs@naver.com")
                    .password("1234")
                    .build();

    findId = userRepository.save(user)
                           .getId();

  }

  @Test
  void save_test() {
    List<String> findWeek = new ArrayList<>();
    findWeek.add("MON");
    findWeek.add("TUE");

    List<String> findCategory = new ArrayList<>();
    findCategory.add("EXERCISE");

    RoutineCreateRequest routineCreateRequest = RoutineCreateRequest.builder()
                                                                    .name("윤동하기")
                                                                    .startTime(LocalDate.now())
                                                                    .durationTime(LocalDate.now())
                                                                    .weeks(findWeek)
                                                                    .routineCategory(findCategory)
                                                                    .color("black")
                                                                    .emoji(">_<")
                                                                    .build();

    RoutineCreateResponse routineCreateResponse = routineService.saveRoutine(findId,
        routineCreateRequest);
    Assertions.assertThat(routineCreateResponse.getName())
              .isEqualTo(routineCreateRequest.getName());

  }

  @Test
  void routineDeleteTest() throws NotFoundException {
    List<String> findWeek = new ArrayList<>();
    findWeek.add("MON");
    findWeek.add("TUE");

    List<String> findCategory = new ArrayList<>();
    findCategory.add("EXERCISE");

    RoutineCreateRequest routineCreateRequest = RoutineCreateRequest.builder()
                                                                    .name("윤동하기")
                                                                    .startTime(LocalDate.now())
                                                                    .durationTime(LocalDate.now())
                                                                    .weeks(findWeek)
                                                                    .routineCategory(findCategory)
                                                                    .color("black")
                                                                    .emoji(">_<")
                                                                    .build();

    Long routineId = routineService.saveRoutine(findId, routineCreateRequest)
                                   .getRoutineId();
    routineService.deleteRoutine(routineId);
    assertThrows(NotFoundException.class, () -> routineService.findId(routineId));
  }

  @Test
  void routineUpdateTest() throws NotFoundException {
    List<String> findWeek = new ArrayList<>();
    findWeek.add("MON");
    findWeek.add("TUE");

    List<String> findCategory = new ArrayList<>();
    findCategory.add("EXERCISE");

    RoutineCreateRequest routineCreateRequest = RoutineCreateRequest.builder()
                                                                    .name("윤동하기")
                                                                    .startTime(LocalDate.now())
                                                                    .durationTime(LocalDate.now())
                                                                    .weeks(findWeek)
                                                                    .routineCategory(findCategory)
                                                                    .color("black")
                                                                    .emoji(">_<")
                                                                    .build();

    Long routineId = routineService.saveRoutine(findId, routineCreateRequest)
                                   .getRoutineId();

    List<String> findWeek2 = new ArrayList<>();
    findWeek2.add("MON");
    findWeek2.add("WED");

    RoutineUpdateRequest routineUpdateRequest = RoutineUpdateRequest.builder()
                                                                    .weeks(findWeek2)
                                                                    .build();
    routineService.updateRoutine(routineId, routineUpdateRequest);
    Routine findRoutine = routineRepository.getById(routineId);
    //Assertions.assertThat(findRoutine.getWeeks()).isEqualTo();

  }
}