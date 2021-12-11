package org.prgrms.yas.domain.user.service;

import java.util.Optional;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserResponse;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final UserRepository userRepository;
	
	public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}
	
	@Transactional(readOnly = true)
	public User signIn(String username, String credentials) {
		User user = userRepository.findByEmail(username)
		                          .orElseThrow(() -> new UsernameNotFoundException("회원이 없습니다."));
		user.checkPassword(
				passwordEncoder,
				credentials
		);
		
		return user;
	}
	
	@Transactional
	public Long signUp(UserSignUpRequest userSignUpRequest) {
		if ((!isDuplicateUser(userSignUpRequest)) && !userSignUpRequest.isDifferentPassword()){
			userSignUpRequest.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));
		}
		return userRepository.save(userSignUpRequest.toEntity())
		                     .getId();
	}
	
	private boolean isDuplicateUser(UserSignUpRequest userSignUpRequest) {
		if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
			throw new RuntimeException("중복 회원입니다.");
		}
		return false;
	}
}
