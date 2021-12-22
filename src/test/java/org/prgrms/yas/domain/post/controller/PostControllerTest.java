package org.prgrms.yas.domain.post.controller;

import static org.hamcrest.Matchers.hasSize;
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
import org.prgrms.yas.setting.setup.PostSetup;
import org.prgrms.yas.setting.setup.RoutineSetup;
import org.prgrms.yas.setting.setup.UserSetup;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(S3Uploader.class)
@ActiveProfiles("test")
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserSetup userSetup;

	@Autowired
	private RoutineSetup routineSetup;

	@Autowired
	private PostSetup postSetup;

	@Test
	@DisplayName("게시글등록_테스트")
	@WithMockJwtAuthentication
	void createTest() throws Exception {
		//given
		PostCreateRequest postCreateRequest = postSetup.buildPostCreateRequest();
		User user = userSetup.saveUser("test@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCA", "name", "nickname");
		Routine routine = routineSetup.saveRoutine(user);
		//when
		ResultActions result = mockMvc.perform(post(
				"/routines/{id}/posts",
				routine.getId()
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
		//given
		User user = userSetup.saveUser("test@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCA", "name", "nickname");
		Routine routine = routineSetup.saveRoutine(user);
		RoutinePost routinePost = postSetup.savePost(routine);
		//when
		ResultActions result = mockMvc.perform(delete(
				"/posts/{id}",
				routinePost.getId()
		).contentType(MediaType.APPLICATION_JSON));

		//then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data").value(routinePost.getId()));
	}

	@Test
	@DisplayName("게시글단건조회_테스트")
	void findOneTest() throws Exception {
		//given
		RoutinePost routinePost = postSetup.savePostDetail();

		//when
		ResultActions result = mockMvc.perform(get(
				"/posts/{id}",
				routinePost.getId()
		).contentType(MediaType.APPLICATION_JSON));

		//then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data.postId").value(routinePost.getId()))
		      .andExpect(jsonPath("$.data.createdAt").isString())
		      .andExpect(jsonPath("$.data.updatedAt").isString())
		      .andExpect(jsonPath("$.data.content").value(routinePost.getContent()))
		      .andExpect(jsonPath("$.data.user.userId").value(routinePost.getRoutine().getUser().getId()))
		      .andExpect(jsonPath("$.data.user.nickname").value(routinePost.getRoutine().getUser().getNickname()))
		      .andExpect(jsonPath("$.data.user.profileImage").value(routinePost.getRoutine().getUser().getProfileImage()))
		      .andExpect(jsonPath("$.data.routine.routineId").value(routinePost.getRoutine().getId()))
		      .andExpect(jsonPath("$.data.routine.name").value(routinePost.getRoutine().getName()))
		      .andExpect(jsonPath("$.data.routine.emoji").value(routinePost.getRoutine().getEmoji()))
		      .andExpect(jsonPath("$.data.routine.durationGoalTime").value(routinePost.getRoutine().getDurationGoalTime()))
		      .andExpect(jsonPath("$.data.routine.color").value(routinePost.getRoutine().getColor()))
		      .andExpect(jsonPath("$.data.routine.weeks", hasSize(2)))
		      .andExpect(jsonPath("$.data.routine.category", hasSize(2)))
		      .andExpect(jsonPath("$.data.likes", hasSize(1)));
	}
}