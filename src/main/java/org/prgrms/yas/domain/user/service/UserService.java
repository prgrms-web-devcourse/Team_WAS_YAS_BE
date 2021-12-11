package org.prgrms.yas.domain.user.service;

import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.repository.UserRepository;
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
}
