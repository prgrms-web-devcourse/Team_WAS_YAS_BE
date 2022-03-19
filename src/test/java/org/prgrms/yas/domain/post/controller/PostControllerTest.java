package org.prgrms.yas.domain.post.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.likes.dto.LikesResponse;
import org.prgrms.yas.domain.likes.repository.PostLikesRepository;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.post.dto.PostCreateRequest;
import org.prgrms.yas.domain.post.exception.NotFoundRoutinePostException;
import org.prgrms.yas.domain.post.repository.PostRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.prgrms.yas.global.error.ErrorCode;
import org.prgrms.yas.setting.security.WithMockJwtAuthentication;
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
	private PostRepository postRepository;
	
	@Autowired
	private PostLikesRepository postLikesRepository;
	
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
		RoutinePost routinePost = postRepository.findById(postId)
		                                        .orElseThrow(() -> new NotFoundRoutinePostException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		List<LikesResponse> likes = postLikesRepository.getByPost(postId);
		//when
		ResultActions result = mockMvc.perform(get(
				"/posts/{id}",
				postId
		).contentType(MediaType.APPLICATION_JSON));
		
		//then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data.postId").value(postId))
		      .andExpect(jsonPath("$.data.createdAt").value(routinePost.getCreatedAt().plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
		      .andExpect(jsonPath("$.data.updatedAt").value(routinePost.getUpdatedAt().plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
		      .andExpect(jsonPath("$.data.content").value(routinePost.getContent()))
		      .andExpect(jsonPath("$.data.user.userId").value(routinePost.getRoutine()
		                                                                 .getUser()
		                                                                 .getId()))
		      .andExpect(jsonPath("$.data.user.nickname").value(routinePost.getRoutine()
		                                                                   .getUser()
		                                                                   .getNickname()))
		      .andExpect(jsonPath("$.data.user.profileImage").value(routinePost.getRoutine()
		                                                                       .getUser()
		                                                                       .getProfileImage()))
		      .andExpect(jsonPath("$.data.routine.routineId").value(routinePost.getRoutine()
		                                                                       .getId()))
		      .andExpect(jsonPath("$.data.routine.name").value(routinePost.getRoutine()
		                                                                  .getName()))
		      .andExpect(jsonPath("$.data.routine.emoji").value(routinePost.getRoutine()
		                                                                   .getEmoji()))
		      .andExpect(jsonPath("$.data.routine.durationGoalTime").value(routinePost.getRoutine()
		                                                                              .getDurationGoalTime()))
		      .andExpect(jsonPath("$.data.routine.color").value(routinePost.getRoutine()
		                                                                   .getColor()))
		      .andExpect(jsonPath("$.data.routine.startGoalTime").value(routinePost.getRoutine()
		                                                                           .getStartGoalTime()
		                                                                           .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))))
		      .andExpect(jsonPath("$.data.routine.weeks[0]").value(routinePost.getRoutine()
		                                                                      .getWeeks()
		                                                                      .get(0)
		                                                                      .toString()))
		      .andExpect(jsonPath("$.data.routine.weeks[1]").value(routinePost.getRoutine()
		                                                                      .getWeeks()
		                                                                      .get(1)
		                                                                      .toString()))
		      .andExpect(jsonPath(
				      "$.data.routine.weeks",
				      hasSize(2)
		      ))
		      .andExpect(jsonPath("$.data.routine.category[0]").value(routinePost.getRoutine()
		                                                                         .getRoutineCategory()
		                                                                         .get(0)
		                                                                         .toString()))
		      .andExpect(jsonPath("$.data.routine.category[1]").value(routinePost.getRoutine()
		                                                                         .getRoutineCategory()
		                                                                         .get(1)
		                                                                         .toString()))
		      .andExpect(jsonPath(
				      "$.data.routine.category",
				      hasSize(2)
		      ))
		      .andExpect(jsonPath("$.data.comments[0].commentId").value(routinePost.getComments()
		                                                                           .get(0)
		                                                                           .getId()))
		      .andExpect(jsonPath("$.data.comments[0].user.userId").value(routinePost.getComments()
		                                                                             .get(0)
		                                                                             .getUser()
		                                                                             .getId()))
		      .andExpect(jsonPath("$.data.comments[0].user.nickname").value(routinePost.getComments()
		                                                                               .get(0)
		                                                                               .getUser()
		                                                                               .getNickname()))
		      .andExpect(jsonPath("$.data.comments[0].user.profileImage").value(routinePost.getComments()
		                                                                                   .get(0)
		                                                                                   .getUser()
		                                                                                   .getProfileImage()))
		      .andExpect(jsonPath("$.data.comments[0].createdAt").value(routinePost.getComments()
		                                                                           .get(0)
		                                                                           .getCreatedAt()
		                                                                           .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))))
		      .andExpect(jsonPath("$.data.comments[0].updatedAt").value(routinePost.getComments()
		                                                                           .get(0)
		                                                                           .getUpdatedAt()
		                                                                           .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))))
		      .andExpect(jsonPath("$.data.comments[0].content").value(routinePost.getComments()
		                                                                         .get(0)
		                                                                         .getContent()))
		      .andExpect(jsonPath("$.data.comments[0].likes[0].userId").value(routinePost.getComments()
		                                                                                 .get(0)
		                                                                                 .getCommentLikes()
		                                                                                 .get(0)
		                                                                                 .getUser()
		                                                                                 .getId()))
		      .andExpect(jsonPath("$.data.comments[0].likes[0].username").value(routinePost.getComments()
		                                                                                   .get(0)
		                                                                                   .getCommentLikes()
		                                                                                   .get(0)
		                                                                                   .getUser()
		                                                                                   .getNickname()))
		      .andExpect(jsonPath(
				      "$.data.comments[0].likes",
				      hasSize(1)
		      ))
		      .andExpect(jsonPath(
				      "$.data.comments",
				      hasSize(1)
		      ))
		      .andExpect(jsonPath("$.data.likes[0].userId").value(likes.get(0)
		                                                               .getUserId()))
		      .andExpect(jsonPath(
				      "$.data.likes",
				      hasSize(1)
		      ));
		
	}
}