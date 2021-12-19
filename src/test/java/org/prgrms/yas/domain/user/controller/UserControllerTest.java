package org.prgrms.yas.domain.user.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.config.TestConfig;
import org.prgrms.yas.configures.JwtConfig;
import org.prgrms.yas.configures.OpenApiConfig;
import org.prgrms.yas.configures.WebSecurityConfig;
import org.prgrms.yas.domain.user.dto.UserSignInRequest;
import org.prgrms.yas.domain.user.dto.UserSignInTestRequest;
import org.prgrms.yas.domain.user.dto.UserSignUpTestRequest;
import org.prgrms.yas.domain.user.service.UserService;
import org.prgrms.yas.global.aws.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestConfig
@Transactional
@Import(S3Uploader.class)
class UserControllerTest {
	
	private static UserSignUpTestRequest userSignUpTestRequest;
	private static UserSignInTestRequest userSignInTestRequest;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private S3Uploader s3Uploader;
	
	@Autowired
	private WebApplicationContext ctx;
	
	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
		                              .addFilters(new CharacterEncodingFilter(
				                              "UTF-8",
				                              true
		                              ))
		                              .build();
		
	}
	
	@DisplayName("회원로그인_테스트")
	@Test
	void signIn() {
		
		// given
//		userSignInTestRequest = new UserSignInTestRequest("")
	}
	
	@DisplayName("회원가입_테스트")
	@Test
	void singUp() throws Exception {
		// given
		userSignUpTestRequest = new UserSignUpTestRequest(
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
		result.andExpect(status().isOk())
		      .andDo(print());
	}
	
	@Test
	void find() {
	}
	
	@Test
	void update() {
	}
	
	@DisplayName("회원탈퇴_테스트")
	@Test
	@WithMockUser("USER")
	void deleteUser() throws Exception {
		// given
		
		// when
//		ResultActions result = mockMvc.perform(delete("/users").header("token",).contentType(MediaType.APPLICATION_JSON))
//		);
		
//		result
//				.andExpect(
//						status().isOk()
//				)
//				.andDo(
//						print()
//				);
	}
}
