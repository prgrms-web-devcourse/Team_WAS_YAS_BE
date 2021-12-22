package org.prgrms.yas.setting.setup;

import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSetup {
	@Autowired
	private UserRepository userRepository;
	
	public User saveUser(String email, String password, String name, String nickname) {
		User user = User.builder()
		                .email(email)
		                .password(password)
		                .name(name)
		                .nickname(nickname)
		                .build();
		return userRepository.save(user);
	}
}
