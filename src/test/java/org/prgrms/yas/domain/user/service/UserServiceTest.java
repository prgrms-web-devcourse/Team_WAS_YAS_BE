package org.prgrms.yas.domain.user.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserPasswordRequest;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.prgrms.yas.global.aws.S3Uploader;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private S3Uploader s3Uploader;
	
	@Test
	@DisplayName("회원 로그인 테스트")
	void signInTest() {
		User user = createUser();
		given(userRepository.findByEmail(any())).willReturn(Optional.of(user));
		given(userRepository.findByEmailAndIsDeletedFalse(any())).willReturn(Optional.of(user));
		User findUser = userService.signIn(
				user.getEmail(),
				user.getPassword()
		);
		
		Assertions.assertThat(user.getEmail())
		          .isEqualTo(findUser.getEmail());
		Assertions.assertThat(user.getName())
		          .isEqualTo(findUser.getName());
		
	}
	
	@Test
	@DisplayName("회원가입 테스트")
	void signUpTest() {
		User user = createUser();
		UserSignUpRequest userSignUpRequest = new UserSignUpRequest("email@email.com",
				"test1231!",
				"test1231!",
				"testNickname",
				"testName");
		
		given(userRepository.save(any())).willReturn(user);
		
		Long aLong = userService.signUp(userSignUpRequest);
		
		Assertions.assertThat(aLong).isNotNull();
		verify(userRepository).save(any());
	}
	
	private static User createUser() {
		User user = Mockito.mock(User.class);
		
		given(user.getId()).willReturn(1L);
		given(user.getPassword()).willReturn("test123!!");
		given(user.getNickname()).willReturn("testNickname");
		given(user.getEmail()).willReturn("test@test.com");
		given(user.getName()).willReturn("testName");
		
		return user;
	}
}
