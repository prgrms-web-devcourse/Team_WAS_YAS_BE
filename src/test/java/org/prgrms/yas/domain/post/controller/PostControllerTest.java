package org.prgrms.yas.domain.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.dto.PostCreateRequest;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.domain.routine.controller.RoutineController;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
import org.prgrms.yas.domain.routine.domain.Week;
import org.prgrms.yas.domain.routine.repository.RoutineRepository;
import org.prgrms.yas.domain.user.controller.UserController;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserSignInRequest;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.prgrms.yas.setting.security.WithMockJwtAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@Import(S3Uploader.class)
class PostControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private PostController postController;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoutineRepository routineRepository;
	
	private Long routineId;
	private Long postId;
	
	@BeforeEach
	void setUp() {
		//User
		User user = User.builder()
		                .email("test@test.com")
		                .password("$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCC")
		                .name("name")
		                .nickname("nickname")
		                .build();
		userRepository.save(user);
		
		//Routine
		List<Week> findWeek = new ArrayList<>();
		findWeek.add(Week.valueOf("MON"));
		findWeek.add(Week.valueOf("TUE"));
		
		List<RoutineCategory> findCategory = new ArrayList<>();
		findCategory.add(RoutineCategory.valueOf("EXERCISE"));
		findCategory.add(RoutineCategory.valueOf("HEALTH"));
		
		Routine routine = Routine.builder()
		                         .user(user)
		                         .name("운동하기")
		                         .startGoalTime(LocalDateTime.now())
		                         .durationGoalTime(12L)
		                         .weeks(findWeek)
		                         .routineCategory(findCategory)
		                         .color("black")
		                         .emoji(">_<")
		                         .build();
		routineId = routineRepository.save(routine)
		                             .getId();
		
		//RoutinePost
		RoutinePost routinePost = RoutinePost.builder()
		                                     .routine(routine)
		                                     .build();
		postId = postRepository.save(routinePost)
		                       .getId();
		
	}
	
	@Test
	@DisplayName("게시글등록_테스트")
	@WithMockJwtAuthentication
	void createTest() throws Exception {
		//given
		PostCreateRequest postCreateRequest = new PostCreateRequest("포스트 내용");
		//when
		ResultActions result = mockMvc.perform(post(
				"/routines/{id}/posts",
				routineId
		).contentType(MediaType.APPLICATION_JSON)
		 .content(objectMapper.writeValueAsString(postCreateRequest)));
		
		// then
		result.andExpect(status().isOk())
		      .andExpect(jsonPath("$.data").isNumber());
	}
	
	
	@Test
	@DisplayName("게시글삭제_테스트")
	@WithMockJwtAuthentication
	void deleteTest() throws Exception {
		
		//when
		ResultActions result = mockMvc.perform(delete(
				"/posts/{id}",
				postId
		).contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data").isNumber());
	}
	
//	@Test
//	@DisplayName("게시글단건조회_테스트")
//	void findOneTest() throws Exception {
//		//when
//		ResultActions result = mockMvc.perform(get(
//				"/posts/{id}",
//				postId
//		).contentType(MediaType.APPLICATION_JSON));
//
//		//then
//		result.andExpect(status().isOk())
//		      .andDo(print())
//		      .andExpect(jsonPath("$.data.").isString())
//		      .andExpect(jsonPath("$.data.userId").isNumber())
//		      .andExpect(jsonPath("$.data.name").isString())
//		      .andExpect(jsonPath("$.data.nickname").isString());
//	}
}