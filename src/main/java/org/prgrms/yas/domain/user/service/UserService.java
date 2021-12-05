package org.prgrms.yas.domain.user.service;

import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.dto.UserJoinRequest;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public Long join(UserJoinRequest userJoinRequest) {
    checkDuplicateUser(userJoinRequest);
    User user = userJoinRequest.toEntity();
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user).getId();
  }

  private void checkDuplicateUser(UserJoinRequest userJoinRequest){
    if(userRepository.existsUserByEmail(userJoinRequest.getEmail())){
      throw new RuntimeException("중복 회원 이메일");
    }
  }
}
