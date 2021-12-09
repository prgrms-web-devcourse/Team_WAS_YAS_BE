package org.prgrms.yas.domain.routine.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.service.RoutineService;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RoutineControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  private RoutineService routineService;
  @Autowired
  private UserRepository userRepository;

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
  void createRoutineTest() throws Exception {
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
                                                                    .build();
    mockMvc.perform(post("/routines").content(objectMapper.writeValueAsString(routineCreateRequest))
                                     .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andDo(print());

  }

  @Test
  void updateRoutineTest() throws Exception {
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

    mockMvc.perform(put("/routines/{id}", routineId).content(
                                                        objectMapper.writeValueAsString(routineUpdateRequest))
                                                    .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andDo(print());
  }

  @Test
  void deleteRoutineTest() throws Exception {
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

    mockMvc.perform(delete("/routines/{id}", routineId).contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andDo(print());
  }

  @Test
  void findRoutinesTest() throws Exception {
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

    mockMvc.perform(get("/routines", findId).contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andDo(print());
  }
}