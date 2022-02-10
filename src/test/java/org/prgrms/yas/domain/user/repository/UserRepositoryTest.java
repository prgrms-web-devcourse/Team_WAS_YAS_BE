package org.prgrms.yas.domain.user.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@DisplayName("회원 DB저장 테스트")
	void saveUserTest() {
		User user = new UserSignUpRequest("test@test.com",
				"test1231!",
				"test1231!",
				"testNickname",
				"testName").toEntity();
		
		User savedUser = userRepository.save(user);
		
		Assertions.assertThat(savedUser).isSameAs(user);
		Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
		Assertions.assertThat(savedUser.getId()).isNotNull();
		Assertions.assertThat(userRepository.count()).isEqualTo(1);
	}
	
	@Test
	@DisplayName("회원 DB삭제 테스트")
	void deleteUserTest(){
		User user = User.builder().email("test@test.com")
				.password("test1231!")
				.nickname("testNickname")
				.name("testName")
				.build();
		userRepository.save(user);
		
		User findUser = userRepository.findByEmail("test@test.com").get();
		userRepository.deleteById(findUser.getId());
		
		Assertions.assertThat(userRepository.count()).isEqualTo(0);
	}
	
	@Test
	@DisplayName("회원 조회 테스트")
	void finduserTest(){
		User user = User.builder().email("test@test.com")
		                .password("test1231!")
		                .nickname("testNickname")
		                .name("testName")
		                .build();
		userRepository.save(user);
		
		User findUser = userRepository.findByEmail("test@test.com").get();
		
		Assertions.assertThat(findUser).isSameAs(user);
		Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
		Assertions.assertThat(findUser.getNickname()).isEqualTo(user.getNickname());
	}
}
