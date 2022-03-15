package org.prgrms.yas.domain.user.repository;

import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.*;
import javax.transaction.TransactionManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown(){
		userRepository.deleteAll();
	}
	
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
		
		User findUser = userRepository.findByEmailAndIsDeletedFalse("test@test.com").get();
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
		
		User findUser = userRepository.findByEmailAndIsDeletedFalse("test@test.com").get();
		
		Assertions.assertThat(findUser).isSameAs(user);
		Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
		Assertions.assertThat(findUser.getNickname()).isEqualTo(user.getNickname());
	}

	@Test
	@DisplayName("소셜로그인 과정 회원 조회 테스트")
	void findByProviderAndProviderId() {

		// given
		User user = User.builder()
				.name("user")
				.email("user@naver.com")
				.provider("kakao")
				.providerId("kakaoId")
				.build();
		userRepository.save(user);

		// when
		User findUser = userRepository.findByProviderAndProviderId(user.getProvider(), user.getProviderId()).get();

		// then
		Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
		Assertions.assertThat(findUser.getEmail()).isEqualTo(user.getEmail());
		Assertions.assertThat(findUser.getProvider()).isEqualTo(user.getProvider());
	}

	@Test
	@DisplayName("Id와 삭제여부를 이용하여 회원 조회 테스트")
	void findByIdAndIsDeletedFalse() {

		// given
		User user = User.builder()
				.name("user")
				.email("user@naver.com")
				.build();
		userRepository.save(user);
		User findUser = userRepository.findByEmailAndIsDeletedFalse(user.getEmail()).get();

		// when
		User checkUser = userRepository.findByIdAndIsDeletedFalse(findUser.getId()).get();

		// then
		assertAll(
				() -> assertEquals(checkUser.getId(),user.getId()),
				() -> assertEquals(checkUser.getName(),user.getName()),
				() -> assertEquals(checkUser.getEmail(), user.getEmail())
		);
	}

	@Test
	@DisplayName("이메일과 삭제여부를 통해 회원 존재 여부 테스트 - True")
	void existsByEmailAndIsDeletedFalse_True() {

		// given
		User user = User.builder()
				.name("user")
				.email("user@naver.com")
				.build();
		userRepository.save(user);

		// when
		boolean result = userRepository.existsByEmailAndIsDeletedFalse(user.getEmail());

		Assertions.assertThat(result).isTrue();
	}

	@Test
	@DisplayName("이메일과 삭제여부를 통해 회원 존재 여부 테스트 - False")
	void existsByEmailAndIsDeletedFalse_False() {

		// given

		// when
		boolean result = userRepository.existsByEmailAndIsDeletedFalse("user@user.com");

		Assertions.assertThat(result).isFalse();
	}

	@Test
	@DisplayName("이메일과 삭제여부를 통해 회원 조회")
	void findByEmailAndIsDeletedFalse() {

		// given
		User user = User.builder()
				.name("user")
				.email("user@naver.com")
				.build();
		userRepository.save(user);

		// when
		User findUser = userRepository.findByEmailAndIsDeletedFalse(user.getEmail()).get();

		// then
		assertAll(
				() -> assertEquals(user.getName(),findUser.getName()),
				() -> assertEquals(user.getEmail(),findUser.getEmail())
		);
	}

	@Test
	@DisplayName("삭제된 회원 전체 조회")
	void findAllByIsDeletedTrue() {

		// given
		User user = User.builder()
				.name("user")
				.email("user@naver.com")
				.build();
		user.updateUserDeleted();
		userRepository.save(user);


		// when
		List<User> findUserList = userRepository.findAllByIsDeletedTrue();

		// then
		assertEquals(findUserList.size(), 1);
	}
}
