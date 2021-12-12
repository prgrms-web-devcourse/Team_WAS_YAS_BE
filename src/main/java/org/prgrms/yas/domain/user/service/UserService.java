package org.prgrms.yas.domain.user.service;

import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserSignUpRequest;
import org.prgrms.yas.domain.user.exception.DuplicateUserException;
import org.prgrms.yas.domain.user.exception.NotFoundUserException;
import org.prgrms.yas.domain.user.repository.UserRepository;

import org.prgrms.yas.global.error.ErrorCode;

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
		                          .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE_ERROR));
		
		user.checkPassword(
				passwordEncoder,
				credentials
		);
		
		return user;
	}
	
	@Transactional
	public Long signUp(UserSignUpRequest userSignUpRequest) {
		if ((!isDuplicateUser(userSignUpRequest)) && !userSignUpRequest.isDifferentPassword()) {
			userSignUpRequest.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));
		}
		return userRepository.save(userSignUpRequest.toEntity())
		                     .getId();
	}
	
	private boolean isDuplicateUser(UserSignUpRequest userSignUpRequest) {
		if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
			throw new DuplicateUserException(ErrorCode.CONFLICT_VALUE_ERROR);
		}
		
		return false;
	}
}
