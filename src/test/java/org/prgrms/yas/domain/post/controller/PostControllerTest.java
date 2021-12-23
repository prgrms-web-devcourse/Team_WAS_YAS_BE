package org.prgrms.yas.domain.post.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.comment.repository.CommentRepository;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.dto.PostCreateRequest;
import org.prgrms.yas.domain.post.exception.NotFoundRoutinePostException;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.setting.security.WithMockJwtAuthentication;
import org.prgrms.yas.setting.setup.PostSetup;
import org.prgrms.yas.setting.setup.RoutineSetup;
import org.prgrms.yas.setting.setup.UserSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@DisplayName("게시글등록_테스트")
	@WithMockJwtAuthentication
	void createTest() throws Exception {
		//given
		PostCreateRequest postCreateRequest = new PostCreateRequest("post");
		Long routineId = 1L;
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
		//given
		Long postId = 1L;
		//when
		ResultActions result = mockMvc.perform(delete(
				"/posts/{id}",
				postId
		).contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data").value(postId));
	}
	
	@Test
	@DisplayName("게시글단건조회_테스트")
	void findOneTest() throws Exception {
		//given
		Long postId = 1L;
		
		//when
		ResultActions result = mockMvc.perform(get(
				"/posts/{id}",
				postId
		).contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data.postId").value(postId))
		      .andExpect(jsonPath("$.data.createdAt").isString())
		      .andExpect(jsonPath("$.data.updatedAt").isString())
		      .andExpect(jsonPath("$.data.content").value("content"))
		      .andExpect(jsonPath("$.data.user.userId").value(1L))
		      .andExpect(jsonPath("$.data.user.nickname").value("nickname"))
		      .andExpect(jsonPath("$.data.user.profileImage").isEmpty())
		      .andExpect(jsonPath("$.data.routine.routineId").value(1L))
		      .andExpect(jsonPath("$.data.routine.name").value("routineName"))
		      .andExpect(jsonPath("$.data.routine.emoji").value("^^"))
		      .andExpect(jsonPath("$.data.routine.durationGoalTime").value(1))
		      .andExpect(jsonPath("$.data.routine.color").value("black"))
		      .andExpect(jsonPath("$.data.routine.startGoalTime").isNotEmpty())
		      .andExpect(jsonPath("$.data.routine.weeks", hasSize(2)))
		      .andExpect(jsonPath("$.data.routine.category", hasSize(2)))
		      .andExpect(jsonPath("$.data.routine.missions", hasSize(1)))
		      .andExpect(jsonPath("$.data.comments", hasSize(1)))
		      .andExpect(jsonPath("$.data.likes", hasSize(1)));
	}
}