package org.prgrms.yas.domain.comment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.comment.domain.Comment;
import org.prgrms.yas.domain.comment.dto.CommentCreateRequest;
import org.prgrms.yas.domain.comment.dto.CommentUpdateRequest;
import org.prgrms.yas.domain.post.domain.RoutinePost;
import org.prgrms.yas.domain.routine.domain.Routine;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.global.aws.S3Uploader;
import org.prgrms.yas.setting.security.WithMockJwtAuthentication;
import org.prgrms.yas.setting.setup.CommentSetup;
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
class CommentControllerTest {
	
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
	private CommentSetup commentSetup;
	
	
	@Test
	@DisplayName("댓글등록_테스트")
	@WithMockJwtAuthentication
	void createTest() throws Exception {
		//given
		User user = userSetup.saveUser("test@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCA", "name", "nickname");
		Routine routine = routineSetup.saveRoutine(user);
		RoutinePost routinePost = postSetup.savePost(routine);
		CommentCreateRequest commentCreateRequest = commentSetup.buildCommentCreateRequest();
		
		//when
		ResultActions result = mockMvc.perform(post(
				"/posts/{id}/comments",
				routinePost.getId()
		).contentType(MediaType.APPLICATION_JSON)
		 .content(objectMapper.writeValueAsString(commentCreateRequest)));
		
		// then
		result.andExpect(status().isOk())
		      .andExpect(jsonPath("$.data").isNumber());
	}
	
	@Test
	@DisplayName("댓글수정_테스트")
	@WithMockJwtAuthentication
	void updateTest() throws Exception {
		//given
		User user_comment = userSetup.saveUser("test@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCA", "name", "nickname");
		User user_post = userSetup.saveUser("test1@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCB", "name1", "nickname1");
		Routine routine = routineSetup.saveRoutine(user_post);
		RoutinePost routinePost = postSetup.savePost(routine);
		
		Comment comment = commentSetup.saveComment(user_comment, routinePost);
		CommentUpdateRequest commentUpdateRequest = commentSetup.buildCommentUpdateRequest();
		//when
		ResultActions result = mockMvc.perform(patch(
				"/comments/{id}",
				comment.getId()
		).contentType(MediaType.APPLICATION_JSON)
		 .content(objectMapper.writeValueAsString(commentUpdateRequest)));
		
		//then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data").value(comment.getId()));
	}
	
	@Test
	@DisplayName("댓글삭제_테스트")
	@WithMockJwtAuthentication
	void deleteTest() throws Exception {
		//given
		User user_comment = userSetup.saveUser("test@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCA", "name", "nickname");
		User user_post = userSetup.saveUser("test1@test.com","$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCB", "name1", "nickname1");
		Routine routine = routineSetup.saveRoutine(user_post);
		RoutinePost routinePost = postSetup.savePost(routine);
		
		Comment comment = commentSetup.saveComment(user_comment, routinePost);
		//when
		ResultActions result = mockMvc.perform(delete(
				"/comments/{id}",
				comment.getId()
		).contentType(MediaType.APPLICATION_JSON));
		System.out.println("comment.getId() "+comment.getId());
		//then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data").value(comment.getId()));
	}
}