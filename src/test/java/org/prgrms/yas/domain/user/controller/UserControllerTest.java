package org.prgrms.yas.domain.user.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserSignInRequest;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.exception.DuplicateUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@Import(S3Uploader.class)
class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp() {
		userRepository.save(User.builder()
		                        .email("test@test.com")
		                        .password("$2a$10$QW.b5MvgypXB5kckcYeYS.ME8kevnoQBHlZxUy8ES4gIzSMOrJkCC")
		                        .name("name")
		                        .nickname("nickname")
		                        .build());
	}
	
	@DisplayName("회원조회_테스트")
	@Test
	void find() throws Exception {
		
		// given
		UserSignInRequest userSignInTestRequest = new UserSignInRequest(
				"test@test.com",
				"password1!"
		);
		
		String token = Objects.requireNonNull(userController.signIn(userSignInTestRequest)
		                                                    .getBody()
		                                                    .getData())
		                      .getToken();
		
		// when
		ResultActions result = mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)
		                                                    .header(
				                                                    "token",
				                                                    token
		                                                    ));
		
		// then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data.email").isString())
		      .andExpect(jsonPath("$.data.userId").isNumber())
		      .andExpect(jsonPath("$.data.name").isString())
		      .andExpect(jsonPath("$.data.nickname").isString());
	}
	
	@DisplayName("중복예외_테스트")
	@Test
	void NotFoundUserExceptionTest() throws Exception {
		// given
		UserSignUpRequest userSignUpTestRequest = new UserSignUpRequest(
				"test@test.com",
				"test123!!",
				"test123!!",
				"testNickname",
				"testName"
		);
		
		// when
		ResultActions result = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
		                                                     .content(objectMapper.writeValueAsString(userSignUpTestRequest)));
		// then
		result.andExpect(status().is4xxClientError())
		      .andExpect(res -> assertTrue(res.getResolvedException()
		                                      .getClass()
		                                      .isAssignableFrom(DuplicateUserException.class)))
		      .andDo(print());
	}
	
	@DisplayName("회원가입_테스트")
	@Test
	void singUp() throws Exception {
		// given
		UserSignUpRequest userSignUpTestRequest = new UserSignUpRequest(
				"test2@test.com",
				"test123!!",
				"test123!!",
				"testNickname",
				"testName"
		);
		
		// when
		ResultActions result = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
		                                                     .content(objectMapper.writeValueAsString(userSignUpTestRequest)));
		// then
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data").isNumber());
	}
	
	@DisplayName("회원탈퇴_테스트")
	@Test
	void deleteUser() throws Exception {
		// given
		UserSignInRequest userSignInTestRequest = new UserSignInRequest(
				"test@test.com",
				"password1!"
		);
		
		String token = Objects.requireNonNull(userController.signIn(userSignInTestRequest)
		                                                    .getBody()
		                                                    .getData())
		                      .getToken();
		// when
		ResultActions result = mockMvc.perform(delete("/users").contentType(MediaType.APPLICATION_JSON)
		                                                       .header(
				                                                       "token",
				                                                       token
		                                                       ));
		
		result.andExpect(status().isOk())
		      .andDo(print())
		      .andExpect(jsonPath("$.data").isNumber());
	}
}
