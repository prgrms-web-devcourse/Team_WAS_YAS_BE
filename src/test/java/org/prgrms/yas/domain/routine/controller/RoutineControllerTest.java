package org.prgrms.yas.domain.routine.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.mission.dto.MissionCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineDetailCreateRequest;
import org.prgrms.yas.domain.routine.dto.RoutineUpdateRequest;
import org.prgrms.yas.domain.routine.service.RoutineService;
import org.prgrms.yas.domain.user.controller.UserController;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserSignInRequest;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.prgrms.yas.global.response.ApiResponse;
import org.prgrms.yas.jwt.JwtAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ActiveProfiles("test")
@SpringBootTest
@Import(S3Uploader.class)
@AutoConfigureMockMvc
@TestConfig
class RoutineControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private UserController userController;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoutineService routineService;
	
	Long userId;
	Long routineId;
	String token;
	
	@BeforeEach
	void setUp() throws Exception {
		userId = userRepository.save(User.builder()
		                                 .email("test@test.com")
		                                 .password("$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCC")
		                                 .name("name")
		                                 .nickname("nickname")
		                                 .build())
		                       .getId();
		
		UserSignInRequest userSignInTestRequest = new UserSignInRequest(
				"test@test.com",
				"password1!"
		);
		
		token = Objects.requireNonNull(userController.signIn(userSignInTestRequest)
		                                             .getBody()
		                                             .getData())
		               .getToken();
		//루틴 생성
		List<String> findWeek = new ArrayList<>();
		findWeek.add("MON");
		findWeek.add("TUE");
		
		List<String> findCategory = new ArrayList<>();
		findCategory.add("EXERCISE");
		
		RoutineCreateRequest routineCreateRequest = RoutineCreateRequest.builder()
		                                                                .name("윤동하기")
		                                                                .durationGoalTime(10L)
		                                                                .startGoalTime(LocalDateTime.now())
		                                                                .weeks(findWeek)
		                                                                .routineCategory(findCategory)
		                                                                .emoji("em")
		                                                                .color("color")
		                                                                .build();
		
		routineId = routineService.saveRoutine(
				userId,
				routineCreateRequest
		);
	}
	
	@Test
	@DisplayName("루틴을 저장 할 수 있다.")
	void createRoutineTest() throws Exception {
		//given
		List<String> findWeek = new ArrayList<>();
		findWeek.add("MON");
		findWeek.add("TUE");
		
		List<String> findCategory = new ArrayList<>();
		findCategory.add("EXERCISE");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		
		System.out.println(formatter.format(ZonedDateTime.now()));
		
		RoutineCreateRequest routineCreateRequest = RoutineCreateRequest.builder()
		                                                                .name("윤동하기")
		                                                                .durationGoalTime(10L)
		                                                                .startGoalTime(null)
		                                                                .weeks(findWeek)
		                                                                .routineCategory(findCategory)
		                                                                .emoji("em")
		                                                                .color("color")
		                                                                .build();
		
		//when
		ResultActions result = mockMvc.perform(post("/routines").content(objectMapper.writeValueAsString(routineCreateRequest))
		                                                        .contentType(MediaType.APPLICATION_JSON)
		                                                        .header(
				                                                        "token",
				                                                        token
		                                                        ));
		//then
		result.andExpect(status().isOk())
		      .andExpect(jsonPath("$.data").isNumber())
		      .andDo(print());
	}
	
	@Test
	@DisplayName("루틴의 요일을 바꿀 수 있다.")
	void updateRoutineTest() throws Exception {
		List<String> findWeek2 = new ArrayList<>();
		findWeek2.add("MON");
		findWeek2.add("WED");
		
		RoutineUpdateRequest routineUpdateRequest = RoutineUpdateRequest.builder()
		                                                                .weeks(findWeek2)
		                                                                .build();
		
		mockMvc.perform(put(
				       "/routines/{id}",
				       routineId
		       ).content(objectMapper.writeValueAsString(routineUpdateRequest))
		        .header(
				        "token",
				        token
		        )
		        .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andDo(print());
	}
	
	@Test
	@DisplayName("루틴을 삭제 할 수 있다. ")
	void deleteRoutineTest() throws Exception {
		mockMvc.perform(delete(
				       "/routines/{id}",
				       routineId
		       ).header(
				        "token",
				        token
		        )
		        .contentType(MediaType.APPLICATION_JSON))
		
		       .andExpect(status().isOk())
		       .andDo(print());
	}
	
	@Test
	@DisplayName("루틴을 상세 조회 할 수 있다. ")
	void getMissionsTest() throws Exception {
		mockMvc.perform(get(
				       "/routines/{id}/missions",
				       routineId
		       ).header(
				        "token",
				        token
		        )
		        .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andDo(print());
	}
	
	@Test
	@DisplayName("루틴 전체 조회 할 수 있다. ")
	void getRoutinesTest() throws Exception {
		ResultActions result = mockMvc.perform(get("/routines").header(
				                                                       "token",
				                                                       token
		                                                       )
		                                                       .contentType(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data.name").isString())
		      .andExpect(jsonPath("$.data.color").isString());
		
	}
	
	@Test
	@DisplayName("루틴을 게시판에서 가져 올 수 있다.")
	void createRoutineFromPostTest() throws Exception {
		List<MissionCreateRequest> missionCreateRequests = new ArrayList<>();
		MissionCreateRequest missionCreateRequest = MissionCreateRequest.builder()
		                                                                .color("color")
		                                                                .emoji("emoji")
		                                                                .durationGoalTime(10L)
		                                                                .name("name")
		                                                                .orders(1)
		                                                                .build();
		missionCreateRequests.add(missionCreateRequest);
		
		List<String> findWeek = new ArrayList<>();
		findWeek.add("MON");
		findWeek.add("TUE");
		
		List<String> findCategory = new ArrayList<>();
		findCategory.add("EXERCISE");
		RoutineDetailCreateRequest routineDetailCreateRequest = RoutineDetailCreateRequest.builder()
		                                                                                  .color("black")
		                                                                                  .durationGoalTime(10L)
		                                                                                  .emoji("emoji")
		                                                                                  .missionCreateRequest(missionCreateRequests)
		                                                                                  .name("루틴")
		                                                                                  .routineCategory(findCategory)
		                                                                                  .weeks(findWeek)
		                                                                                  .startGoalTime(null)
		                                                                                  .build();
		
		ResultActions result = mockMvc.perform(post("/routines").content(objectMapper.writeValueAsString(routineDetailCreateRequest))
		                                                        .contentType(MediaType.APPLICATION_JSON)
		                                                        .header(
				                                                        "token",
				                                                        token
		                                                        ))
		                              .andExpect(status().isOk())
		                              .andDo(print());
		
	}
	
}